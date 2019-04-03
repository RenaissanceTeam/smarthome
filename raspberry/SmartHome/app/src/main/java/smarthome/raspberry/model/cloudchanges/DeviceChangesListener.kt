package smarthome.raspberry.model.cloudchanges

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.library.common.IotDevice
import smarthome.library.datalibrary.store.SmartHomeStorage
import smarthome.library.datalibrary.store.listeners.DevicesObserver
import smarthome.raspberry.BuildConfig
import smarthome.raspberry.OddDeviceInCloud
import smarthome.raspberry.model.SmartHomeRepository

object DeviceChangesListener : DevicesObserver {

    private val ioScope = CoroutineScope(Dispatchers.IO)

    override fun onDevicesChanged(cloudDevices: MutableList<IotDevice>?, isInner: Boolean) {
        if (BuildConfig.DEBUG) Log.d(SmartHomeRepository.TAG, "new devices update isInner=$isInner, $cloudDevices ")
        if (isInner || cloudDevices == null) return
        ioScope.launch { tryHandleChanges(cloudDevices) }
    }

    private suspend fun tryHandleChanges(cloudDevices: MutableList<IotDevice>) {
        try {
            handleChanges(cloudDevices, SmartHomeRepository.devicesStorage)
        } catch (e: Throwable) {
            if (BuildConfig.DEBUG) Log.w(SmartHomeRepository.TAG, "while handling changes: ", e)
        }
    }

    private suspend fun handleChanges(cloudDevices: List<IotDevice>, storage: SmartHomeStorage) {
        for (device in cloudDevices) {
            val localDevice = SmartHomeRepository.devices.find { it == device }
                    ?: throw OddDeviceInCloud(device)

            val changesHandler = DeviceChangesHandler(localDevice, device)
            changesHandler.handleChanges()

            if (changesHandler.changesMade) {
                storage.updateDevice(localDevice)
            }
        }
    }

}