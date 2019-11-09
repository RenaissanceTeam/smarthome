package smarthome.raspberry.devices.data

import smarthome.library.common.IotDevice
import smarthome.raspberry.devices.data.storage.IotDeviceGroup
import smarthome.raspberry.devices.data.storage.LocalStorage
import smarthome.raspberry.devices.data.storage.RemoteStorage
import smarthome.raspberry.devices_api.data.DevicesRepository

class DevicesRepositoryImpl(
        private val localStorage: LocalStorage,
        private val remoteStorage: RemoteStorage
): DevicesRepository {
    override suspend fun saveDevice(device: IotDevice) {
        localStorage.updateDevice(device, IotDeviceGroup.ACTIVE)
        remoteStorage.updateDevice(device)
    }

    override suspend fun addPendingDevice(device: IotDevice) {
        localStorage.addDevice(device, IotDeviceGroup.PENDING)
        remoteStorage.addPendingDevice(device)
    }

    override suspend fun removePendingDevice(device: IotDevice) {
        localStorage.removeDevice(device, IotDeviceGroup.PENDING)
        remoteStorage.removePendingDevice(device)
    }

    override suspend fun addDevice(device: IotDevice) {
        localStorage.addDevice(device, IotDeviceGroup.ACTIVE)
        remoteStorage.addDevice(device)
    }

    override suspend fun removeDevice(device: IotDevice) {
        localStorage.removeDevice(device, IotDeviceGroup.PENDING)
        remoteStorage.removeDevice(device)
    }

    override suspend fun getCurrentDevices(): List<IotDevice> {
        return localStorage.getDevices()
    }
}