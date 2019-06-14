package smarthome.client.data

import io.reactivex.Observable
import smarthome.client.domain.HomeRepository
import smarthome.library.common.IotDevice
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
}