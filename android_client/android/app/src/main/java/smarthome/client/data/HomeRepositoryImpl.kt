package smarthome.client.data

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import smarthome.client.domain.HomeRepository
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.library.common.scripts.Script

class HomeRepositoryImpl(private val localStorage: LocalStorage,
                         private val remoteStorage: RemoteStorage) : HomeRepository {
    private val ioScope = CoroutineScope(Dispatchers.IO)

    override suspend fun getDevices(): Observable<MutableList<IotDevice>> {
        return localStorage.getDevices()
        // todo don't forget to listen for remote changes when have at least 1 subscriber
    }

    override suspend fun observeDevicesUpdates() {
        remoteStorage.observeDevices { devices, isInner ->
            if (isInner) return@observeDevices

            ioScope.launch { localStorage.saveDevices(devices) }
        }
    }

    override suspend fun saveAppToken(newToken: String) {
        localStorage.saveAppToken(newToken)
        remoteStorage.saveAppToken(newToken)
    }

    override suspend fun getSavedAppToken(): String? {
         return localStorage.getAppToken()
    }

    override suspend fun getControllers(): MutableList<BaseController> {
        val devices = localStorage.getDevices().value
        if (devices.isNullOrEmpty()) return mutableListOf()

        return devices.flatMap { it.controllers }.toMutableList()
    }

    override suspend fun updateDevice(device: IotDevice) {
        remoteStorage.updateDevice(device)
        localStorage.updateDevice(device)
    }

    override suspend fun getDevice(deviceGuid: Long): IotDevice? {
        val devices = localStorage.getDevices().value
        if (devices.isNullOrEmpty()) return null

        return devices.find { it.guid == deviceGuid }
    }

    override suspend fun getPendingController(controllerGuid: Long): BaseController? {
        val devices = localStorage.getPendingDevices().value
        if (devices.isNullOrEmpty()) return null

        for (device in devices) {
            return device.controllers.find { it.guid == controllerGuid } ?: continue
        }

        return null
    }

    override suspend fun getPendingDevices(): BehaviorSubject<MutableList<IotDevice>> {
        return localStorage.getPendingDevices()
    }

    override suspend fun updatePendingDevice(device: IotDevice) {
        remoteStorage.updatePendingDevice(device)
        localStorage.updatePendingDevice(device)
    }

    override suspend fun getScripts(): BehaviorSubject<MutableList<Script>> {
        return localStorage.getScripts()
    }

    override suspend fun saveScript(script: Script) {
        remoteStorage.saveScript(script)
        localStorage.saveScript(script)
    }
}