package smarthome.raspberry.data.local.devices

import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapterFactory
import smarthome.library.common.IotDevice
import smarthome.raspberry.data.local.LocalDevicesStorage




class LocalDevicesStorageImpl(typeAdapterFactory: TypeAdapterFactory) :
        LocalDevicesStorage {
    var savedData: String? = null
    private val gson = GsonBuilder().registerTypeAdapterFactory(typeAdapterFactory).create()
    private val devicesType = object : TypeToken<ArrayList<IotDevice>>() {}.type

    override fun saveDevices(devices: List<IotDevice>) {
        savedData = gson.toJson(devices, devicesType)
    }

    override fun getSavedDevices(): List<IotDevice> {
        return gson.fromJson(savedData, devicesType) ?: listOf()
    }
}