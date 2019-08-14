package smarthome.raspberry.data.local.devices

import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapterFactory
import smarthome.library.common.IotDevice
import smarthome.raspberry.data.local.LocalDevicesStorage

class LocalDevicesStorageImpl(typeAdapterFactory: TypeAdapterFactory) :
        LocalDevicesStorage {
    private val gson = GsonBuilder().registerTypeAdapterFactory(typeAdapterFactory).create()
    private val devicesType = object : TypeToken<ArrayList<IotDevice>>() {}.type
    private var savedDevicesData: String? = null
    private var savedPendingDevicesData: String? = null
    private val devices: MutableList<IotDevice> = initializeSavedDevices()
    private val pendingDevices: MutableList<IotDevice> = mutableListOf()

    private fun initializeSavedDevices(): MutableList<IotDevice> {
        return gson.fromJson(savedDevicesData, devicesType) ?: mutableListOf()
    }

    override fun saveDevices(devices: List<IotDevice>) {
        savedDevicesData = gson.toJson(devices, devicesType)
    }

    override fun getSavedDevices(): List<IotDevice> {
        return devices
    }

    override fun updateDevice(device: IotDevice) {
        devices[devices.indexOf(device)] = device
    }

    override fun add(device: IotDevice) {
        devices.add(device)
        saveDevices(devices)
    }

    override fun addPending(device: IotDevice) {
        pendingDevices.add(device)
    }

    override fun removePending(device: IotDevice) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(device: IotDevice) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
