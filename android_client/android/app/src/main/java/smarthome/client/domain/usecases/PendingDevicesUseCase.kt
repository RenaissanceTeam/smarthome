package smarthome.client.domain.usecases

import io.reactivex.Observable
import smarthome.client.domain.HomeRepository
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

class PendingDevicesUseCase(private val repository: HomeRepository) {
    fun getPendingDevices(): Observable<MutableList<IotDevice>> {
        TODO()
    }

    fun findPendingDevice(controller: BaseController): IotDevice {
        TODO()
    }

    fun changePendingDevice(device: IotDevice) {
        TODO()
    }

    fun getPendingDevice(deviceGuid: Long): IotDevice? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}