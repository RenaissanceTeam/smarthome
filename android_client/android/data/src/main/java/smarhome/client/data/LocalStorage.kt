package smarhome.client.data

import io.reactivex.subjects.BehaviorSubject
import smarthome.library.common.IotDevice
import smarthome.library.common.scripts.Script


interface LocalStorage {
    suspend fun saveDevices(devices: MutableList<IotDevice>)
    suspend fun getDevices(): BehaviorSubject<MutableList<IotDevice>>
    suspend fun saveAppToken(newToken: String)
    suspend fun getAppToken(): String?
    suspend fun updateDevice(device: IotDevice)
    suspend fun getPendingDevices(): BehaviorSubject<MutableList<IotDevice>>
    suspend fun updatePendingDevice(device: IotDevice)
    suspend fun getScripts(): BehaviorSubject<MutableList<Script>>
    suspend fun saveScript(script: Script)
    suspend fun getSavedHomeId(): String?

}

