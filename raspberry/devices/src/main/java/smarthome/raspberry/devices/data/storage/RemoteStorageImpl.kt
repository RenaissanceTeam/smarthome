package smarthome.raspberry.devices.data.storage

import smarthome.library.common.IotDevice
import smarthome.library.common.SmartHomeStorage

class RemoteStorageImpl (
    private val homeStorage: SmartHomeStorage
) : RemoteStorage {
    override suspend fun updateDevice(device: IotDevice) {
        homeStorage.updateDevice(device)
    }

    override suspend fun addPendingDevice(device: IotDevice) {
        homeStorage.addPendingDevice(device)
    }

    override suspend fun removePendingDevice(device: IotDevice) {
        homeStorage.removePendingDevice(device)
    }

    override suspend fun addDevice(device: IotDevice) {
        homeStorage.addDevice(device)
    }

    override suspend fun removeDevice(device: IotDevice) {
        homeStorage.removeDevice(device)
    }
}