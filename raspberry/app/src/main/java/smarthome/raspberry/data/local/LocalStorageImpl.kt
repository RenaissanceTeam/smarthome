package smarthome.raspberry.data.local

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice
import smarthome.raspberry.data.LocalStorage

class LocalStorageImpl(private val preferences: SharedPreferencesHelper,
                       private val localDevicesStorage: LocalDevicesStorage
) : LocalStorage {

    private val homeId = BehaviorSubject.create<String>()

    init {
        if (preferences.hasSavedHomeId()) {
            homeId.onNext(preferences.getHomeId())
        }
    }

    override fun getDevices(): MutableList<IotDevice> {
        return localDevicesStorage.getSavedDevices(IotDeviceGroup.ACTIVE).toMutableList()
    }

    override fun getHomeId(): Observable<String> {
        return homeId
    }

    override suspend fun saveHome(homeId: String) {
        preferences.setHomeId(homeId)
        this.homeId.onNext(homeId)
    }

    override fun getPendingDevices(): MutableList<IotDevice> {
        return localDevicesStorage.getSavedDevices(IotDeviceGroup.PENDING).toMutableList()
    }

    override fun findDevice(controller: BaseController): IotDevice {
        return getDevices().find { it.controllers.contains(controller) } ?: TODO()
    }

    override fun updateDevice(device: IotDevice) {
        localDevicesStorage.updateDevice(device, IotDeviceGroup.ACTIVE)
    }

    override suspend fun addDevice(device: IotDevice) {
        localDevicesStorage.add(device, IotDeviceGroup.ACTIVE)
    }

    override suspend fun addPendingDevice(device: IotDevice) {
        localDevicesStorage.add(device, IotDeviceGroup.PENDING)
    }

    override suspend fun removePendingDevice(device: IotDevice) {
        localDevicesStorage.remove(device, IotDeviceGroup.PENDING)
    }

    override suspend fun removeDevice(device: IotDevice) {
        localDevicesStorage.remove(device, IotDeviceGroup.ACTIVE)
    }
}