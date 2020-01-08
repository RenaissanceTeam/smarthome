package smarthome.client.data.api.home

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import smarthome.client.domain.api.entity.Controller
import smarthome.client.domain.api.entity.Device
import smarthome.client.domain.api.entity.Script

interface HomeRepository {
    suspend fun getDevices(): Observable<MutableList<Device>>

    suspend fun observeDevicesUpdates()
    suspend fun saveAppToken(newToken: String)
    suspend fun getSavedAppToken(): String?
    suspend fun getControllers(): MutableList<Controller>
    suspend fun updateDevice(device: Device)
    suspend fun getDevice(deviceGuid: Long): Device?
    suspend fun getPendingController(controllerGuid: Long): Controller?
    suspend fun getPendingDevices(): BehaviorSubject<MutableList<Device>>
    suspend fun updatePendingDevice(device: Device)
    suspend fun getScripts(): BehaviorSubject<MutableList<Script>>
    suspend fun saveScript(script: Controller)
    suspend fun getSavedHomeId(): String?
}