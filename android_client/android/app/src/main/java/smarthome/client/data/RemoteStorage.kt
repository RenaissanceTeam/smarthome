package smarthome.client.data

import io.reactivex.Observable
import smarthome.library.common.IotDevice
import smarthome.library.common.scripts.Script
import smarthome.library.datalibrary.store.listeners.DeviceUpdate

interface RemoteStorage  {
    suspend fun observeDevices(): Observable<DeviceUpdate>
    suspend fun saveAppToken(newToken: String)
    suspend fun updateDevice(device: IotDevice)
    suspend fun updatePendingDevice(device: IotDevice)
    suspend fun saveScript(script: Script)

}

