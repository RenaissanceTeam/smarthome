package smarthome.client.data.home

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import smarthome.client.data.api.home.HomeRepository
import smarthome.client.entity.Controller
import smarthome.client.entity.Device
import smarthome.client.entity.Script

class HomeRepositoryImpl : HomeRepository {
    override suspend fun getDevices(): Observable<MutableList<Device>> {
        return Observable.just(mutableListOf())
    }
    
    override suspend fun observeDevicesUpdates() {
    
    }
    
    override suspend fun saveAppToken(newToken: String) {
    
    }
    
    override suspend fun getSavedAppToken(): String? {
        return "asdf"
    }
    
    override suspend fun getControllers(): MutableList<Controller> {
        return mutableListOf()
    }
    
    override suspend fun updateDevice(device: Device) {
    
    }
    
    override suspend fun getDevice(deviceGuid: Long): Device? {
        TODO()
    }
    
    override suspend fun getPendingController(controllerGuid: Long): Controller? {
        TODO()
    }
    
    override suspend fun getPendingDevices(): BehaviorSubject<MutableList<Device>> {
        TODO()
    }
    
    override suspend fun updatePendingDevice(device: Device) {
    
    }
    
    override suspend fun getScripts(): BehaviorSubject<MutableList<Script>> {
        TODO()
    }
    
    override suspend fun saveScript(script: Controller) {
    
    }
    
    override suspend fun getSavedHomeId(): String? {
        return "sdaf"
    }
}