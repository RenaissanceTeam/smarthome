package smarthome.client.domain.usecases

import io.reactivex.Observable
import smarthome.client.domain.HomeRepository
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

class PendingDevicesUseCase(private val repository: HomeRepository) {
    fun getPendingDevices(): Observable<MutableList<IotDevice>> {
        TODO()
    }

    fun getPendingDevice(controller: BaseController): IotDevice {
        TODO()
    }

    fun changePendingDevice(device: IotDevice) {
        TODO()
    }
}