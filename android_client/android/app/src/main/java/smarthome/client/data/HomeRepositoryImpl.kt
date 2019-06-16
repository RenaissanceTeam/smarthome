package smarthome.client.data

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import smarthome.client.domain.HomeRepository
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.library.common.scripts.Script
import smarthome.library.datalibrary.store.listeners.DevicesObserver

class HomeRepositoryImpl(private val localStorage: LocalStorage,
                         private val remoteStorage: RemoteStorage) : HomeRepository {

    override suspend fun getDevices(): Observable<MutableList<IotDevice>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        // todo don't forget to listen for remote changes when have at least 1 subscriber
    }

    override fun observeDevicesUpdates(devicesObserver: DevicesObserver) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override suspend fun saveAppToken(newToken: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAppToken(): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getControllers(): MutableList<BaseController> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updateDevice(device: IotDevice) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getDevice(deviceGuid: Long): IotDevice? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getPendingController(controllerGuid: Long): BaseController? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPendingDevices(): BehaviorSubject<MutableList<IotDevice>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun updatePendingDevice(device: IotDevice) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getScripts(): BehaviorSubject<MutableList<Script>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun saveScript(script: Script) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}