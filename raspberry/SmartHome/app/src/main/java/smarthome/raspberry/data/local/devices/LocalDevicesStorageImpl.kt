package smarthome.raspberry.data.local.devices

import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapterFactory
import smarthome.library.common.IotDevice
import smarthome.raspberry.data.local.IotDeviceGroup
import smarthome.raspberry.data.local.LocalDevicesStorage

class LocalDevicesStorageImpl(typeAdapterFactory: TypeAdapterFactory) :
        LocalDevicesStorage {
    private val gson = GsonBuilder().registerTypeAdapterFactory(typeAdapterFactory).create()
    private val devicesType = object : TypeToken<ArrayList<IotDevice>>() {}.type
    private var savedDevicesData: MutableMap<IotDeviceGroup, String> = mutableMapOf()
    private val devices: MutableMap<IotDeviceGroup, MutableList<IotDevice>> = mutableMapOf()

    init {
        for (group in IotDeviceGroup.values()) {
            devices[group] = gson.fromJson(savedDevicesData[group], devicesType) ?: mutableListOf()
        }
    }

    override fun saveDevices(devices: MutableList<IotDevice>, group: IotDeviceGroup) {
        savedDevicesData[group] = gson.toJson(devices, devicesType)
    }

    override fun getSavedDevices(group: IotDeviceGroup): List<IotDevice> {
        return devices[group] ?: listOf()
    }

    override fun updateDevice(device: IotDevice, group: IotDeviceGroup) {
        val list = devices[group] ?: TODO()

        list[list.indexOf(device)] = device
    }

    override fun add(device: IotDevice, group: IotDeviceGroup) {
        val list = devices[group] ?: mutableListOf()
        list.add(device)
        saveDevices(list, group)
    }

    override fun remove(device: IotDevice, group: IotDeviceGroup) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}