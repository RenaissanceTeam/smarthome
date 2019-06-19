package smarthome.client.domain

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.library.common.scripts.Script

interface HomeRepository {
    suspend fun getDevices(): Observable<MutableList<IotDevice>>

    suspend fun observeDevicesUpdates()
    suspend fun saveAppToken(newToken: String)
    suspend fun getSavedAppToken(): String?
    suspend fun getControllers(): MutableList<BaseController>
    suspend fun updateDevice(device: IotDevice)
    suspend fun getDevice(deviceGuid: Long): IotDevice?
    suspend fun getPendingController(controllerGuid: Long): BaseController?
    suspend fun getPendingDevices(): BehaviorSubject<MutableList<IotDevice>>
    suspend fun updatePendingDevice(device: IotDevice)
    suspend fun getScripts(): BehaviorSubject<MutableList<Script>>
    suspend fun saveScript(script: Script)
    suspend fun getSavedHomeId(): String?
}