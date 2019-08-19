package smarthome.raspberry.input

import io.reactivex.Observable
import smarthome.library.common.DeviceUpdate

interface InputControllerDataSource {
    suspend fun getDeviceUpdates(): Observable<DeviceUpdate>
}