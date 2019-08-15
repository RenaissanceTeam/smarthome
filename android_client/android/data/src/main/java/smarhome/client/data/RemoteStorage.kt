package smarhome.client.data

import io.reactivex.Observable
import smarthome.library.common.DeviceUpdate
import smarthome.library.common.IotDevice
import smarthome.library.common.scripts.Script

interface RemoteStorage  {
    suspend fun observeDevices(): Observable<DeviceUpdate>
    suspend fun saveAppToken(newToken: String)
    suspend fun updateDevice(device: IotDevice)
    suspend fun updatePendingDevice(device: IotDevice)
    suspend fun saveScript(script: Script)
    suspend fun getAllHomeIds(): MutableList<String>

}

