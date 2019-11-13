package smarthome.raspberry.devices.domain

import smarthome.library.common.IotDevice
import smarthome.raspberry.devices.data.DevicesRepository
import smarthome.raspberry.devices_api.domain.DevicesService

class DevicesServiceImpl(private val repository: DevicesRepository) : DevicesService {

    /**
     * Triggered when new device should be added to the smarthome.
     *
     * New device is considered to be pending until user explicitly accepts it.
     */
    override suspend fun addNewDevice(device: IotDevice) {
        val devices = repository.getCurrentDevices()

        if (devices.contains(device)) {
            TODO()
        }

        repository.addPendingDevice(device)
    }

    /**
     * Accepting device means making it a part of the smarthome, after which it should
     * work in it's normal state: user should be able to make read/write requests, create
     * scripts with the device, etc..
     */
    override suspend fun acceptPendingDevice(device: IotDevice) {
        repository.removePendingDevice(device)
        repository.addDevice(device)
    }

    override suspend fun removeDevice(device: IotDevice) {
        repository.removeDevice(device)
    }

    override suspend fun saveDevice(device: IotDevice) {
        repository.saveDevice(device)
    }

    override suspend fun getCurrentDevices(): List<IotDevice> {
        return repository.getCurrentDevices()
    }
}

