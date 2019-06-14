package smarthome.client.model

import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.library.common.scripts.Script


interface CloudMessaging {
    fun saveInstanceToken(token: String)

}


interface Storage: ScriptStorage, PendingDevicesStorage, DevicesStorage


interface ScriptStorage {
    fun saveScript(script: Script)
    fun getScript(guid: Long): Script?

}
interface PendingDevicesStorage {
    suspend fun getPendingDevice(guid: Long): IotDevice
    suspend fun getPendingDevice(controller: BaseController): IotDevice

        suspend fun getPendingDevices(): MutableList<IotDevice>

}

interface DevicesStorage {

}