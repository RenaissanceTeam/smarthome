package smarthome.raspberry.model

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.library.common.BaseController
import smarthome.library.common.GUID
import smarthome.library.common.IotDevice
import smarthome.library.common.SmartHome
import smarthome.library.common.message.Message
import smarthome.library.datalibrary.model.InstanceToken
import smarthome.library.datalibrary.store.InstanceTokenStorage
import smarthome.library.datalibrary.store.MessageQueue
import smarthome.library.datalibrary.store.SmartHomeStorage
import smarthome.library.datalibrary.store.firestore.FirestoreMessageQueue
import smarthome.raspberry.BuildConfig.DEBUG
import smarthome.raspberry.FirestoreUnreachable
import smarthome.raspberry.UnableToCreateMessageQueue
import smarthome.raspberry.arduinodevices.ArduinoDevice
import smarthome.raspberry.arduinodevices.controllers.ArduinoController
import smarthome.raspberry.model.cloudchanges.DeviceChangesListener
import smarthome.raspberry.model.listeners.RepoInitListener
import smarthome.raspberry.service.GatewayServiceController
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.constants.IDLE_STATUS
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.Gateway
import smarthome.raspberry.thirdpartydevices.xiaomi.gateway.device.GatewayDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.YeelightDevice
import smarthome.raspberry.thirdpartydevices.xiaomi.yeelight.controller.Controller
import smarthome.raspberry.utils.HomeController
import smarthome.raspberry.utils.SharedPreferencesHelper
import smarthome.raspberry.utils.fcm.FcmSender
import smarthome.raspberry.utils.fcm.MessageType
import smarthome.raspberry.utils.fcm.Priority
import java.util.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


@SuppressLint("StaticFieldLeak")
object SmartHomeRepository : SmartHome() {
    const val TAG = "SmartHomeRepository"
    private var listener: RepoInitListener? = null
    lateinit var devicesStorage: SmartHomeStorage
    private lateinit var context: Context
    private lateinit var tokenStorage: InstanceTokenStorage
    lateinit var messageQueue: MessageQueue
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private var tokens: List<InstanceToken> = listOf()
    private lateinit var fcmSender: FcmSender // todo don't like violation of SRP in repo

    private var ready: Boolean = false
    private val pendingDevices: Queue<IotDevice> = LinkedList()

    suspend fun init(appContext: Context, listener: RepoInitListener) {
        setInitListener(listener)
        init(appContext)
    }

    suspend fun init(appContext: Context) {
        context = appContext

        ioScope.launch {
            val homeController = HomeController(context)
            val homeId = homeController.getHomeId()
            devicesStorage = homeController.getSmartHomeStorage(homeId)
            tokenStorage = homeController.getTokenStorage(homeId)
            messageQueue = FirestoreMessageQueue.getInstance(homeId)
                    ?: throw UnableToCreateMessageQueue(homeId)
            devices = ArrayList()
            fcmSender = FcmSender(context)

            loadSavedDevices()

            listener?.onInitializationComplete()
        }
    }

    fun setInitListener(listener: RepoInitListener) {
        this.listener = listener
    }

    private fun loadSavedDevices() {
        val gatewayDevices: MutableSet<GatewayDevice> = mutableSetOf()
        for (dataSource in DataSources.values()) {
            dataSource.init(context)
            devices.addAll(dataSource.source.all)
            devices.forEach { device ->
                (device as? ArduinoDevice)?.controllers?.forEach {
                    (it as? ArduinoController)?.device = device
                }

                if (device is YeelightDevice) {
                    device.controllers.forEach {
                        (it as Controller).device = device
                    }
                }

                if (GatewayDevice::class.java.isAssignableFrom(device.javaClass))
                    gatewayDevices.add(device as GatewayDevice)

                device.controllers.forEach {
                    it.classType = it.javaClass.simpleName
                }
            }
        }

        if (gatewayDevices.isNotEmpty())
            processGatewayDevices(gatewayDevices)

        ready = true
        processPendingDevices()
    }

    private fun processGatewayDevices(gatewayDevices: MutableSet<GatewayDevice>) {
        if (DEBUG) Log.d(TAG, "processing saved gateway devices")
        val sidToDeviceMap: HashMap<String, MutableList<GatewayDevice>> = hashMapOf()
        val devices = gatewayDevices.toMutableList()
        devices.forEach {
            val sid = it.parentGatewaySid
            if (sidToDeviceMap[sid] == null)
                sidToDeviceMap[sid] = mutableListOf()

            sidToDeviceMap[sid]!!.add(it)
        }

        for (device in sidToDeviceMap[IDLE_STATUS]!!) {
            val gateway = device as Gateway
            if (DEBUG) Log.d(TAG, "creating gateway service for gateway: $gateway")
            GatewayServiceController.getInstance().bindGatewayService(gateway, sidToDeviceMap[gateway.sid])
        }
    }

    fun listenForCloudChanges() {
        if (DEBUG) Log.d(TAG, "listenForCloudChanges")
        devicesStorage.observeDevicesUpdates(DeviceChangesListener)
        devicesStorage.observePendingDevicesUpdates(DeviceChangesListener)
        tokenStorage.observeTokenChanges { tokens = it }
    }

    fun subscribeToMessageQueue() {
        messageQueue.subscribe(MessageHandler.getInstance())
        if (DEBUG) Log.d(TAG, "Successfully subscribed to message queue")
    }

    fun deleteMessage(message: Message) {
        messageQueue.removeMessage(message)
        messageQueue.removeMessage(message)
    }

    private fun deleteLocalDevices(cloudDevices: MutableList<IotDevice>) {
        // some devices were deleted
        val removed = mutableListOf<IotDevice>()
        for (localDevice in devices) {
            if (cloudDevices.contains(localDevice)) continue
            removed.add(localDevice)
        }
        removed.forEach { delete(it) } // todo add delete() to repository
    }


    /**
     * Main entry point for adding devices, call this method, when new
     * Iot device should be added to databases
     */
    fun addDevice(device: IotDevice): Boolean {
        if (!ready) {
            pendingDevices.add(device)
            return false
        }

        if (DEBUG) Log.d(TAG, "addDevice: $device")

        for (dataSource in DataSources.values()) {
            if (!device.belongsTo(dataSource)) continue

            dataSource.init(context)

            val src = dataSource.source
            if (src.contains(device)) {
                if (src.get(device.guid).status != device.status) {
                    ioScope.launch {
                        suspendCoroutine<Unit> { continuation ->
                            if (device.isAccepted) {
                                movePendingDeviceToAccepted(device, continuation)
                                src.update(device)
                                devices[devices.indexOf(device)] = device
                            } else if (device.isRejected) {
                                checkAndProcessGateway(device)
                                delete(device)
                            }
                        }
                    }
                }

                src.update(device)
                devices[devices.indexOf(device)] = device

                return true
            }

            if (!dataSource.source.add(device)) continue

            devices.add(device)
            ioScope.launch {
                suspendCoroutine<Unit> { continuation ->
                    if (device.isPending)
                        pushPendingDeviceToCloud(device, continuation)
                    else if (device.isAccepted)
                        pushAcceptedDeviceToCloud(device, continuation)
                }
            }

            return true
        }

        return false
    }

    fun delete(device: IotDevice) {
        for (dataSource in DataSources.values()) {
            if (dataSource.deviceType != device.javaClass)
                continue

            devices.remove(device)
            ioScope.launch {
                suspendCoroutine<Unit> { continuation ->
                    dataSource.source.delete(device)
                    removeDevice(device, continuation)
                }
            }
            ioScope.launch {
                suspendCoroutine<Unit> {
                    removePendingDevice(device, it)
                }
            }
        }
    }

    private fun checkAndProcessGateway(device: IotDevice) {
        if (device is Gateway)
            ioScope.launch {
                GatewayServiceController.getInstance().removeGatewayService(device)
            }
    }

    fun changeDeviceStatus(deviceId: Long, status: String) {
        val device = devices.find { it.guid == deviceId }

        if (device != null) {
            device.status = status
            addDevice(device)
        }

    }

    private fun pushAcceptedDeviceToCloud(device: IotDevice, continuation: Continuation<Unit>) {
        devicesStorage.addDevice(device,
                OnSuccessListener {
                    if (DEBUG) Log.d(TAG, "success adding device to firestore")
                    continuation.resumeWith(Result.success(Unit))
                },
                OnFailureListener {
                    if (DEBUG) Log.d(TAG, "failed $it")
                    continuation.resumeWith(Result.failure(it))
                }
        )
    }

    private fun pushPendingDeviceToCloud(device: IotDevice, continuation: Continuation<Unit>) {
        devicesStorage.addPendingDevice(device,
                OnSuccessListener {
                    if (DEBUG) Log.d(TAG, "success adding pending device to firestore")
                    continuation.resumeWith(Result.success(Unit))
                },
                OnFailureListener {
                    if (DEBUG) Log.d(TAG, "failed $it")
                    continuation.resumeWith(Result.failure(it))
                }
        )
    }

    private fun movePendingDeviceToAccepted(device: IotDevice, continuation: Continuation<Unit>) {
        devicesStorage.addDevice(device,
                OnSuccessListener {
                    devicesStorage.removePendingDevice(device,
                            OnSuccessListener {
                                if (DEBUG) Log.d(TAG, "device successfully moved from pending to root devices node")
                                continuation.resumeWith(Result.success(Unit))
                            },
                            OnFailureListener {
                                if (DEBUG) Log.d(TAG, "failed $it")
                                continuation.resumeWith(Result.failure(it))
                            })
                },
                OnFailureListener {
                    if (DEBUG) Log.d(TAG, "failed $it")
                    continuation.resumeWith(Result.failure(it))
                })
    }

    private fun removeDevice(device: IotDevice, continuation: Continuation<Unit>) {
        devicesStorage.removeDevice(device,
                OnSuccessListener {
                    if (DEBUG) Log.d(TAG, "device successfully removed")
                    continuation.resumeWith(Result.success(Unit))
                },
                OnFailureListener {
                    if (DEBUG) Log.d(TAG, "failed $it")
                    continuation.resumeWith(Result.failure(it))
                })
    }

    private fun removePendingDevice(device: IotDevice, continuation: Continuation<Unit>) {
        devicesStorage.removePendingDevice(device,
                OnSuccessListener {
                    if (DEBUG) Log.d(TAG, "pending device successfully removed")
                    continuation.resumeWith(Result.success(Unit))
                },
                OnFailureListener {
                    if (DEBUG) Log.d(TAG, "failed $it")
                    continuation.resumeWith(Result.failure(it))
                }
        )
    }


    private suspend fun addDeviceToStorage(device: IotDevice) {
        suspendCoroutine<Unit> { continuation ->
            devicesStorage.addDevice(device,
                    OnSuccessListener {
                        if (DEBUG) Log.d(TAG, "success adding device to firestore")
                        continuation.resumeWith(Result.success(Unit))
                    },
                    OnFailureListener {
                        if (DEBUG) Log.d(TAG, "failed $it")
                        continuation.resumeWithException(it)
                    }
            )
        }
    }

    fun getController(guid: Long): BaseController {
        var controller: BaseController? = null
        devices.find {
            controller = it.controllers.find { it.guid == guid }
            controller != null
        }
        return controller ?: throw IllegalArgumentException("No controller with guid=$guid")
    }

    fun getArduinoByIp(ip: String): ArduinoDevice {
        return devices.find { (it as? ArduinoDevice)?.ip == ip } as? ArduinoDevice
                ?: throw IllegalArgumentException("No device with ip=$ip")
    }

    fun removeAll() {
        DataSources.values().forEach { it.source.clearAll() }

        for (device in devices) {
            GUID.getInstance().remove(device.guid)
            device.controllers.forEach { GUID.getInstance().remove(it.guid) }
        }

        devices.clear()
    }

    private fun processPendingDevices() {
        while (pendingDevices.size > 0)
            addDevice(pendingDevices.poll())
    }

    fun handleAlert(device: IotDevice, controller: BaseController) {
        // todo save to firestore (notify android client, send FCM)
        ioScope.launch {
            try {
                updateDeviceInRemoteStorage(device, device.isPending)

                var messageType = MessageType.NOTIFICATION
                if (SharedPreferencesHelper.getInstance(context).getDoNotDisturb(
                                FirebaseAuth.getInstance().currentUser!!.uid)) //TODO: for all users in home
                    messageType = MessageType.DATA

                fcmSender.send(controller, device, messageType, Priority.HIGH,
                        tokens.map { it.token }.toTypedArray())
            } catch (e: Throwable) {
                if (DEBUG) Log.d(TAG, "can't handle alert: ", e)
            }
        }
    }

    private suspend fun updateDeviceInRemoteStorage(device: IotDevice, isPending: Boolean) {
        suspendCoroutine<Unit> { c ->
            if (isPending) {
                devicesStorage.updatePendingDevice(device,
                        OnSuccessListener { c.resumeWith(Result.success(Unit)) },
                        OnFailureListener { c.resumeWithException(FirestoreUnreachable()) })
            }
            else {
                devicesStorage.updateDevice(device,
                        OnSuccessListener { c.resumeWith(Result.success(Unit)) },
                        OnFailureListener { c.resumeWithException(FirestoreUnreachable()) })
            }
        }
    }
}

private fun IotDevice.belongsTo(dataSource: DataSources) = dataSource.deviceType == this.javaClass
