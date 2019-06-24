package smarthome.raspberry.data.local

import android.content.Context
import smarthome.library.common.IotDevice
import smarthome.raspberry.data.LocalStorage
import smarthome.raspberry.utils.SharedPreferencesHelper

class LocalStorageImpl(private val context: Context): LocalStorage {
    private val sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context)
    private val input: LocalStorageInput = TODO()
    private val output: LocalStorageOutput = TODO()
    private val devices: MutableList<IotDevice> = mutableListOf()

    override fun getDevices(): MutableList<IotDevice> {
        return devices
    }

    override suspend fun getHomeId(): String {
        if (!sharedPreferencesHelper.isHomeIdExists()) {
            saveNewHomeId()
        }
        return sharedPreferencesHelper.getHomeId()
    }

    private suspend fun saveNewHomeId() {
        val homeId = input.generateHomeId()
        sharedPreferencesHelper.setHomeId(homeId)

        output.createHome(homeId)
    }
}