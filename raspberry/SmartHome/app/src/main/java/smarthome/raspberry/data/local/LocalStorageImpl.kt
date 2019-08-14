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
    private val savedDevices: MutableList<IotDevice> by lazy { initializeSavedDevices() }
    private val pendingDevices: MutableList<IotDevice> = mutableListOf()


    private fun initializeSavedDevices(): MutableList<IotDevice> {
        return localDevicesStorage.getSavedDevices().toMutableList()
    }

    override fun getDevices(): MutableList<IotDevice> {
        return savedDevices
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
        return savedDevices.find { it.controllers.contains(controller) } ?: TODO()
    }

    override fun updateDevice(device: IotDevice) {
        savedDevices[savedDevices.indexOf(device)] = device
        updateSavedDevicesInStorage()
    }

    private fun updateSavedDevicesInStorage() {
        localDevicesStorage.saveDevices(savedDevices)
    }

    override suspend fun addDevice(device: IotDevice) {
        savedDevices.add(device)
        updateSavedDevicesInStorage()
    }

    override suspend fun addPendingDevice(device: IotDevice) {
        pendingDevices.add(device)
    }

    override suspend fun removePendingDevice(device: IotDevice) {
        pendingDevices.remove(device)
    }

    override suspend fun removeDevice(device: IotDevice) {
        savedDevices.remove(device)
        updateSavedDevicesInStorage()

    }
}