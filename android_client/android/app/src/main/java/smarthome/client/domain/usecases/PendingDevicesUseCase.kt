package smarthome.client.domain.usecases

import io.reactivex.Observable
import smarthome.client.domain.HomeRepository
import smarthome.client.util.NoDeviceException
import smarthome.client.util.NoDeviceWithControllerException
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

class PendingDevicesUseCase(private val repository: HomeRepository) {
    fun getPendingDevices(): Observable<MutableList<IotDevice>> {
        return repository.getPendingDevices()
    }

    fun findPendingDevice(controller: BaseController): IotDevice {
        val pendingDevices = getPendingDevicesFromRepo()

        return pendingDevices.find { it.controllers.contains(controller) } ?: throw NoDeviceWithControllerException(controller)
    }

    private fun getPendingDevicesFromRepo(): MutableList<IotDevice> {
        return repository.getPendingDevices().value
                ?: TODO("no pending devices in behavior subject")
    }

    suspend fun changePendingDevice(device: IotDevice) {
        repository.updatePendingDevice(device)
    }

    fun getPendingDevice(deviceGuid: Long): IotDevice {
        val pendingDevices = getPendingDevicesFromRepo()

        return pendingDevices.find { it.guid == deviceGuid } ?: throw NoDeviceException(deviceGuid)
    }
}