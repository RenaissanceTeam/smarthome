package smarthome.client.domain

import io.reactivex.Observable
import smarthome.library.common.IotDevice
import smarthome.library.datalibrary.store.listeners.DevicesObserver

interface HomeRepository {
    suspend fun getDevices(): Observable<MutableList<IotDevice>>

    fun observeDevicesUpdates(devicesObserver: DevicesObserver)
}