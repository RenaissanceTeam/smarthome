package smarthome.raspberry.data

import smarthome.library.common.IotDevice

interface LocalStorage {
    suspend fun getHomeId(): String
    fun getDevices(): MutableList<IotDevice>
}