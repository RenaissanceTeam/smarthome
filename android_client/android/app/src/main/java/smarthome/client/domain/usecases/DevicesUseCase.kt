package smarthome.client.domain.usecases

import io.reactivex.Observable
import smarthome.client.domain.HomeRepository
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

class DevicesUseCase(private val repository: HomeRepository) {

    suspend fun getDevices(): Observable<MutableList<IotDevice>>{
        return repository.getDevices()
    }

    fun changeDevice(device: IotDevice) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun findDevice(devices: MutableList<IotDevice>?, changedController: BaseController): IotDevice? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getDevice(deviceGuid: Long): IotDevice? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

