package smarthome.raspberry.data.local

import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.raspberry.data.LocalStorage
import smarthome.raspberry.data.LocalStorageInput
import smarthome.raspberry.data.LocalStorageOutput

class LocalStorageImpl(private val preferences: SharedPreferencesHelper,
                       private val input: LocalStorageInput,
                       private val output: LocalStorageOutput,
                       private val localDevicesStorage: LocalDevicesStorage
) : LocalStorage {

    override fun getDevices(): MutableList<IotDevice> {
        return localDevicesStorage.getSavedDevices().toMutableList()
    }

    override suspend fun getHomeId(): String {
        if (!preferences.isHomeIdExists()) {
            saveNewHomeId()
        }
        return preferences.getHomeId()
    }

    private suspend fun saveNewHomeId() {
        val homeId = input.generateHomeId()
        preferences.setHomeId(homeId)

        output.createHome(homeId)
    }

    override fun findDevice(controller: BaseController): IotDevice {
        return getDevices().find { it.controllers.contains(controller) } ?: TODO()
    }

    override fun updateDevice(device: IotDevice) {
        localDevicesStorage.updateDevice(device)
    }

    override suspend fun addDevice(device: IotDevice) {
        localDevicesStorage.add(device)
    }

    override suspend fun addPendingDevice(device: IotDevice) {
        localDevicesStorage.addPending(device)
    }

    override suspend fun removePendingDevice(device: IotDevice) {
        localDevicesStorage.removePending(device)
    }

    override suspend fun removeDevice(device: IotDevice) {
        localDevicesStorage.remove(device)
    }
}