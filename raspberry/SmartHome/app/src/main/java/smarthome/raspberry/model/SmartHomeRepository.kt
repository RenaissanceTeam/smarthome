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
import smarthome.library.datalibrary.store.SmartHomeStorage
import smarthome.library.datalibrary.store.listeners.DevicesObserver
import smarthome.raspberry.BuildConfig.DEBUG
import smarthome.raspberry.OddDeviceInCloud
import smarthome.raspberry.arduinodevices.ArduinoDevice
import smarthome.raspberry.arduinodevices.controllers.ArduinoController
import smarthome.raspberry.utils.HomeController
import java.util.*
import kotlin.coroutines.suspendCoroutine


@SuppressLint("StaticFieldLeak")
object SmartHomeRepository : SmartHome() {
    const val TAG = "SmartHomeRepository"
    lateinit var context: Context
    lateinit var storage: SmartHomeStorage
    private val ioScope = CoroutineScope(Dispatchers.IO)

    fun init(appContext: Context) {
        context = appContext

        ioScope.launch {
            val homeController = HomeController(context)
            val homeId = homeController.getHomeId()
            storage = homeController.getSmartHomeStorage(homeId)

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
    }

    suspend fun listenForCloudChanges() {
        if (DEBUG) Log.d(TAG, "listenForCloudChanges")

        storage.observeDevicesUpdates(DevicesObserver { cloudDevices, isInner ->
            if (isInner) return@DevicesObserver
            ioScope.launch {
                if (DEBUG) Log.d(TAG, "new devices update: $cloudDevices isInner=$isInner")

                if (cloudDevices.size < devices.size) {
                    deleteLocalDevices(cloudDevices)
                }

                try {
                    handleChanges(cloudDevices, storage)
                } catch (e: Throwable) {
                    if (DEBUG) Log.w(TAG, "while handling changes: ", e)
                }
            }
        })
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
//        removed.forEach { repository.delete(it) } // todo add delete() to repository
    }


    /**
     * Main entry point for adding devices, call this method, when new
     * Iot device should be added to databases
     */
    fun addDevice(device: IotDevice): Boolean {
        if (DEBUG) Log.d(TAG, "addDevice: $device")

        for (dataSource in DataSources.values()) {
            if (dataSource.deviceType != device.javaClass) {
                continue
            }

            if (dataSource.source.contains(device)) {
                dataSource.source.update(device)
                devices[devices.indexOf(device)] = device
                return true
            }

            val wasAdded = dataSource.source.add(device)
            if (wasAdded) {
                // todo set alarms for auto refresh for each controller

                devices.add(device)
                ioScope.launch {
                    suspendCoroutine<Unit> { continuation ->
                        storage.addDevice(device,
                                OnSuccessListener {
                                    if (DEBUG) Log.d(TAG, "success adding device to firestore")
                                    continuation.resumeWith(Result.success(Unit))
                                },
                                OnFailureListener { if (DEBUG) Log.d(TAG, "failed $it")
                                    continuation.resumeWith(Result.failure(it))
                                }
                        )
                    }
                }
                return true
            }

        }
        return false
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
}
