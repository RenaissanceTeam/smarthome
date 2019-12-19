package smarthome.raspberry.devices.data.storage

import smarthome.library.common.IotDevice

class LocalStorageImpl : LocalStorage {
    private val devices: MutableMap<IotDeviceGroup, MutableList<IotDevice>> = mutableMapOf()
    
    override fun updateDevice(device: IotDevice, group: IotDeviceGroup) {
        val groupDevices = devices[group] ?: return
        
        groupDevices[groupDevices.indexOf(device)] = device
    }
    
    override fun getDeviceGroup(device: IotDevice): IotDeviceGroup {
        return devices.filterValues { it.contains(device) }.keys.takeIf { it.isNotEmpty() }?.first()
            ?: TODO()
    }
    
    override fun addDevice(device: IotDevice, group: IotDeviceGroup) {
        val groupDevices = devices[group] ?: mutableListOf<IotDevice>().also { devices[group] = it }
        groupDevices.add(device)
    }
    
    override fun removeDevice(device: IotDevice, group: IotDeviceGroup) {
        devices[group]?.remove(device)
    }
    
    override fun getDevices(group: IotDeviceGroup): List<IotDevice> {
        return devices[group].orEmpty()
    }
    
    override fun getDevices(): List<IotDevice> {
        return devices.flatMap { it.value }
    }
}