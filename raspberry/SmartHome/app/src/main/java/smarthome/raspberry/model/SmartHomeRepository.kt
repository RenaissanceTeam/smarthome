package smarthome.raspberry.model

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.library.common.BaseController
import smarthome.library.common.GUID
import smarthome.library.common.IotDevice
import smarthome.library.common.SmartHome
import smarthome.library.common.constants.ACCEPTED
import smarthome.library.common.constants.DENIED
import smarthome.library.common.constants.PENDING
import smarthome.library.datalibrary.store.MessageQueue
import smarthome.library.datalibrary.store.SmartHomeStorage
import smarthome.library.datalibrary.store.firestore.FirestoreMessageQueue
import smarthome.library.datalibrary.store.listeners.DevicesObserver
import smarthome.library.datalibrary.store.listeners.MessageListener
import smarthome.raspberry.BuildConfig.DEBUG
import smarthome.raspberry.OddDeviceInCloud
import smarthome.raspberry.UnableToCreateHomeStorage
import smarthome.raspberry.UnableToCreateMessageQueue
import smarthome.raspberry.arduinodevices.ArduinoDevice
import smarthome.raspberry.arduinodevices.controllers.ArduinoController
import smarthome.raspberry.utils.HomeController
import java.util.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.suspendCoroutine


@SuppressLint("StaticFieldLeak")
object SmartHomeRepository : SmartHome() {
    const val TAG = "SmartHomeRepository"
    lateinit var context: Context
    lateinit var storage: SmartHomeStorage
    lateinit var messageQueue: MessageQueue
    private val ioScope = CoroutineScope(Dispatchers.IO)

    private var ready: Boolean = false
    private val pendingDevices: Queue<IotDevice> = LinkedList()

    fun init(appContext: Context) {
        context = appContext

        ioScope.launch {
            val homeController = HomeController(context)
            val homeId = homeController.getHomeId()
            storage = homeController.getSmartHomeStorage(homeId)
            messageQueue = FirestoreMessageQueue.getInstance(homeId) ?: throw UnableToCreateMessageQueue(homeId)

            devices = ArrayList()

            loadSavedDevices()
        }
    }

    private fun loadSavedDevices() {
        for (dataSource in DataSources.values()) {
            dataSource.init(context)
            devices.addAll(dataSource.source.all)
            devices.forEach { device ->
                (device as? ArduinoDevice)?.controllers?.forEach {
                    (it as? ArduinoController)?.device = device
                }
                // todo add code here to fix some links after deserializing from db
            }
        }

        ready = true
        processPendingDevices()
    }

    suspend fun listenForCloudChanges() {
        if (DEBUG) Log.d(TAG, "listenForCloudChanges")

        storage.observeDevicesUpdates(DevicesObserver { cloudDevices, isInner ->
            if (isInner) return@DevicesObserver
            ioScope.launch {
                if (DEBUG) Log.d(TAG, "new devices update: $cloudDevices isInner=$isInner")

                try {
                    handleChanges(cloudDevices, storage)
                } catch (e: Throwable) {
                    if (DEBUG) Log.w(TAG, "while handling changes: ", e)
                }
            }
        })
    }

    suspend fun subscribeToMessageQueue() {
        messageQueue.subscribe(MessageHandler.getInstance())
        if (DEBUG) Log.d(TAG, "Successfully subscribed to message queue")
    }

    private suspend fun handleChanges(cloudDevices: List<IotDevice>, storage: SmartHomeStorage) {
        for (device in cloudDevices) {
            val localDevice = devices.find { it == device } ?: throw OddDeviceInCloud(device)

            val changesHandler = DeviceChangesHandler(localDevice, device)
            changesHandler.handleChanges()

            if (changesHandler.changesMade) {
                storage.updateDevice(localDevice)
            }
        }
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
            if (dataSource.deviceType != device.javaClass) {
                continue
            }

            dataSource.init(context)

            val src = dataSource.source
            if (src.contains(device)) {
                if (src.get(device.guid).status != device.status) {
                    ioScope.launch {
                        suspendCoroutine<Unit> { continuation ->
                            if (device.status == ACCEPTED) {
                                movePendingDeviceToAccepted(device, continuation)
                                src.update(device)
                                devices[devices.indexOf(device)] = device
                            }

                            else if (device.status == DENIED) {
                                removePendingDevice(device, continuation)
                                delete(device)
                            }
                        }
                    }
                }

                src.update(device)
                devices[devices.indexOf(device)] = device

                return true
            }

            val wasAdded = dataSource.source.add(device)
            if (wasAdded) {
                // todo set alarms for auto refresh for each controller

                devices.add(device)
                ioScope.launch {
                    suspendCoroutine<Unit> { continuation ->
                        if (device.status == PENDING)
                            pushPendingDeviceToCloud(device, continuation)
                        else if (device.status == ACCEPTED)
                            pushAcceptedDeviceToCloud(device, continuation)
                    }
                }

                return true
            }

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
                    storage.removeDevice(device,
                            OnSuccessListener {
                                if (DEBUG) Log.d(TAG, "device successfully removed")
                                continuation.resumeWith(Result.success(Unit))
                            },
                            OnFailureListener {
                                if (DEBUG) Log.d(TAG, "failed $it")
                                continuation.resumeWith(Result.failure(it))
                            })
                }
            }
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
        storage.addDevice(device,
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
        storage.addPendingDevice(device,
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
        storage.addDevice(device,
                OnSuccessListener {
                    storage.removePendingDevice(device,
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

    private fun removePendingDevice(device: IotDevice, continuation: Continuation<Unit>) {
        storage.removePendingDevice(device,
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

    fun removeAll() { // это что за убийственный метод
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
}
