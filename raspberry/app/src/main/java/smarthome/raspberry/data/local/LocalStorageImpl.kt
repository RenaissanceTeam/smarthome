package smarthome.raspberry.data.local

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.raspberry.data.LocalStorage

class LocalStorageImpl(private val preferences: smarthome.raspberry.util.SharedPreferencesHelper,
                       private val localDevicesStorage: smarthome.raspberry.devices.data.storage.LocalDevicesStorage
) : LocalStorage {


    override suspend fun saveHome(homeId: String) {
        preferences.setHomeId(homeId)
        this.homeId.onNext(homeId)
    }

    override fun getPendingDevices(): MutableList<IotDevice> {
        return localDevicesStorage.getSavedDevices(
                smarthome.raspberry.devices.data.storage.IotDeviceGroup.PENDING).toMutableList()
    }

    override fun findDevice(controller: BaseController): IotDevice {
        return getDevices().find { it.controllers.contains(controller) } ?: TODO()
    }

    override fun updateDevice(device: IotDevice) {
        localDevicesStorage.updateDevice(device, smarthome.raspberry.devices.data.storage.IotDeviceGroup.ACTIVE)
    }

    override suspend fun addDevice(device: IotDevice) {
        localDevicesStorage.add(device, smarthome.raspberry.devices.data.storage.IotDeviceGroup.ACTIVE)
    }

    override suspend fun addPendingDevice(device: IotDevice) {
        localDevicesStorage.add(device, smarthome.raspberry.devices.data.storage.IotDeviceGroup.PENDING)
    }

    override suspend fun removePendingDevice(device: IotDevice) {
        localDevicesStorage.remove(device, smarthome.raspberry.devices.data.storage.IotDeviceGroup.PENDING)
    }

    override suspend fun removeDevice(device: IotDevice) {
        localDevicesStorage.remove(device, smarthome.raspberry.devices.data.storage.IotDeviceGroup.ACTIVE)
    }
}