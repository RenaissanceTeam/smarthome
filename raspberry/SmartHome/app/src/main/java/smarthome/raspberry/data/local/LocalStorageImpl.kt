package smarthome.raspberry.data.local

import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.raspberry.data.LocalStorage

class LocalStorageImpl : LocalStorage {
    private val preferences: SharedPreferencesHelper = TODO()
    private val input: LocalStorageInput = TODO()
    private val output: LocalStorageOutput = TODO()
    private val devices: MutableList<IotDevice> = mutableListOf()

    override fun getDevices(): MutableList<IotDevice> {
        return devices
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
        return devices.find { it.controllers.contains(controller) } ?: TODO()
    }

    override fun saveDevice(device: IotDevice) {
        devices[devices.indexOf(device)] = device
    }
}