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
import smarthome.library.datalibrary.model.InstanceToken
import smarthome.library.datalibrary.store.InstanceTokenStorage
import smarthome.library.datalibrary.store.SmartHomeStorage
import smarthome.raspberry.BuildConfig.DEBUG
import smarthome.raspberry.FirestoreUnreachable
import smarthome.raspberry.arduinodevices.ArduinoDevice
import smarthome.raspberry.arduinodevices.controllers.ArduinoController
import smarthome.raspberry.model.cloudchanges.DeviceChangesListener
import smarthome.raspberry.utils.HomeController
import smarthome.raspberry.utils.fcm.FcmSender
import smarthome.raspberry.utils.fcm.MessageType
import smarthome.raspberry.utils.fcm.Priority
import java.util.*
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


@SuppressLint("StaticFieldLeak")
object SmartHomeRepository : SmartHome() {
    const val TAG = "SmartHomeRepository"
    lateinit var devicesStorage: SmartHomeStorage
    private lateinit var context: Context
    private lateinit var tokenStorage: InstanceTokenStorage
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private var tokens: List<InstanceToken> = listOf()
    private lateinit var fcmSender: FcmSender // todo don't like violation of SRP in repo

    private var ready: Boolean = false
    private val pendingDevices: Queue<IotDevice> = LinkedList()

    fun init(appContext: Context) {
        context = appContext

        ioScope.launch {
            val homeController = HomeController(context)
            val homeId = homeController.getHomeId()
            devicesStorage = homeController.getSmartHomeStorage(homeId)
            tokenStorage = homeController.getTokenStorage(homeId)
            devices = ArrayList()
            fcmSender = FcmSender(context)

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

    fun listenForCloudChanges() {
        if (DEBUG) Log.d(TAG, "listenForCloudChanges")
        devicesStorage.observeDevicesUpdates(DeviceChangesListener)
        tokenStorage.observeTokenChanges { tokens = it }
    }


    /**
     * Main entry point for adding devices, call this method, when new
     * Iot device should be added to databases
     */
    fun addDevice(device: IotDevice): Boolean {
        if (DEBUG) Log.d(TAG, "addDevice: $device")

        if (!ready) {
            pendingDevices.add(device)
            return false
        }

        for (dataSource in DataSources.values()) {
            if (!device.belongsTo(dataSource)) continue

            dataSource.init(context)

            if (dataSource.source.contains(device)) {
                dataSource.source.update(device)
                devices[devices.indexOf(device)] = device
                return true
            }

            if (!dataSource.source.add(device)) continue

            // todo set alarms for auto refresh for each controller

            devices.add(device)
            ioScope.launch {
                try {
                    addDeviceToStorage(device)
                } catch (e: Throwable) {
                    if (DEBUG) Log.d(TAG, "addDevice failure: ", e)
                }
            }
            return true
        }
        return false
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
                updateDeviceInRemoteStorage(device)
                fcmSender.send(controller, device, MessageType.NOTIFICATION, Priority.HIGH,
                        tokens.map { it.token }.toTypedArray())
            } catch (e: Throwable) {
                if (DEBUG) Log.d(TAG, "can't handle alert: ", e)
            }
        }
    }

    private suspend fun updateDeviceInRemoteStorage(device: IotDevice) {
        suspendCoroutine<Unit> { c ->
            devicesStorage.updateDevice(device,
                    OnSuccessListener { c.resumeWith(Result.success(Unit))},
                    OnFailureListener { c.resumeWithException(FirestoreUnreachable()) })
        }
    }
}

private fun IotDevice.belongsTo(dataSource: DataSources) = dataSource.deviceType == this.javaClass
