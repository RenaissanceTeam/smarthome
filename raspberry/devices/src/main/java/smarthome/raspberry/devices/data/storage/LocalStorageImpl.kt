package smarthome.raspberry.devices.data.storage

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import smarthome.library.common.IotDevice
import smarthome.raspberry.util.SharedPreferencesHelper

class LocalStorageImpl(
        private val preferences: SharedPreferencesHelper
) : LocalStorage {

    private val gson = GsonBuilder().registerTypeAdapterFactory(typeAdapterFactory).create()
    private val devicesType = object : TypeToken<ArrayList<IotDevice>>() {}.type
    private var savedDevicesData: MutableMap<IotDeviceGroup, String> = mutableMapOf()
    private val devices: MutableMap<IotDeviceGroup, MutableList<IotDevice>> = mutableMapOf()

    init {
        for (group in IotDeviceGroup.values()) {
            devices[group] = gson.fromJson(savedDevicesData[group], devicesType) ?: mutableListOf()
        }
    }



    override fun updateDevice(device: IotDevice) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addPendingDevice(device: IotDevice) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removePendingDevice(device: IotDevice) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addDevice(device: IotDevice) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeDevice(device: IotDevice) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDevices(): List<IotDevice> {
        return devices.flatMap { it.value }
    }
}