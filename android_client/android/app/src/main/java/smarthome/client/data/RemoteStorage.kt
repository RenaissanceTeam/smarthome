package smarthome.client.data

import smarthome.library.common.IotDevice
import smarthome.library.common.scripts.Script
import smarthome.library.datalibrary.store.listeners.DevicesObserver

interface RemoteStorage  {
    suspend fun observeDevices(devicesObserver: DevicesObserver)
    suspend fun saveAppToken(newToken: String)
    suspend fun getAppToken(): String?
    suspend fun updateDevice(device: IotDevice)
    suspend fun updatePendingDevice(device: IotDevice)
    suspend fun saveScript(script: Script)

}

