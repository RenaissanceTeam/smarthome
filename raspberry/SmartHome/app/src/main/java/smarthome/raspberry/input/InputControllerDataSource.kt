package smarthome.raspberry.input

import io.reactivex.Observable
import smarthome.library.common.DeviceUpdate

interface InputControllerDataSource {
    fun getDeviceUpdates(): Observable<DeviceUpdate>
}