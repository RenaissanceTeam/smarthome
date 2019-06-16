package smarthome.client.domain

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.library.common.scripts.Script
import smarthome.library.datalibrary.store.listeners.DevicesObserver

interface HomeRepository {
    suspend fun getDevices(): Observable<MutableList<IotDevice>>

    fun observeDevicesUpdates(devicesObserver: DevicesObserver)
    suspend fun saveAppToken(newToken: String)
    suspend fun getAppToken(): String?
    suspend fun getControllers(): MutableList<BaseController>
    suspend fun updateDevice(device: IotDevice)
    suspend fun getDevice(deviceGuid: Long): IotDevice?
    suspend fun getPendingController(controllerGuid: Long): BaseController?
    fun getPendingDevices(): BehaviorSubject<MutableList<IotDevice>>
    suspend fun updatePendingDevice(device: IotDevice)
    fun getScripts(): BehaviorSubject<MutableList<Script>>
    suspend fun saveScript(script: Script)
}