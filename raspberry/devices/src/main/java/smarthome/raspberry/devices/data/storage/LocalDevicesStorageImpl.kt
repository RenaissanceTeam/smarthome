package smarthome.raspberry.devices.data.storage

import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapterFactory
import smarthome.library.common.IotDevice

class LocalDevicesStorageImpl(typeAdapterFactory: TypeAdapterFactory) :
        LocalDevicesStorage {


    override fun saveDevices(devices: MutableList<IotDevice>, group: IotDeviceGroup) {
        savedDevicesData[group] = gson.toJson(devices, devicesType)
    }

    override fun getSavedDevices(group: IotDeviceGroup): List<IotDevice> {
    }

    override fun updateDevice(device: IotDevice, group: IotDeviceGroup) {
        val list = devices[group] ?: return

        list[list.indexOf(device)] = device
    }

    override fun add(device: IotDevice, group: IotDeviceGroup) {
        val list = devices[group] ?: mutableListOf()
        list.add(device)
        saveDevices(list, group)
    }

    override fun remove(device: IotDevice, group: IotDeviceGroup) {
        val list = devices[group] ?: mutableListOf()
        list.remove(device)

        saveDevices(list, group)
    }
}