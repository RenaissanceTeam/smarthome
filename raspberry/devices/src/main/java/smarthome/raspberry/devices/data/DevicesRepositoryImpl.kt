package smarthome.raspberry.devices.data

import smarthome.library.common.IotDevice
import smarthome.raspberry.devices.data.storage.LocalStorage
import smarthome.raspberry.devices.data.storage.RemoteStorage
import smarthome.raspberry.devices_api.data.DevicesRepository

class DevicesRepositoryImpl(
        private val localStorage: LocalStorage,
        private val remoteStorage: RemoteStorage
): DevicesRepository {
    override suspend fun saveDevice(device: IotDevice) {
        localStorage.updateDevice(device)
        remoteStorage.updateDevice(device)
    }

    override suspend fun addPendingDevice(device: IotDevice) {
        localStorage.addPendingDevice(device)
        remoteStorage.addPendingDevice(device)
    }

    override suspend fun removePendingDevice(device: IotDevice) {
        localStorage.removePendingDevice(device)
        remoteStorage.removePendingDevice(device)
    }

    override suspend fun addDevice(device: IotDevice) {
        localStorage.addDevice(device)
        remoteStorage.addDevice(device)
    }

    override suspend fun removeDevice(device: IotDevice) {
        localStorage.removeDevice(device)
        remoteStorage.removeDevice(device)
    }

    override suspend fun getCurrentDevices(): List<IotDevice> {
        return localStorage.getDevices()
    }
}