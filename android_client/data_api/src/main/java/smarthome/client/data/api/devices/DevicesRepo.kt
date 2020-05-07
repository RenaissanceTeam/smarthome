package smarthome.client.data.api.devices

import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.entity.Device

interface DevicesRepo {
    suspend fun getAdded(): List<GeneralDeviceInfo>
    suspend fun getById(deviceId: Long): Device
    suspend fun getPending(): List<GeneralDeviceInfo>
    suspend fun acceptPending(id: Long)
    suspend fun declinePending(id: Long)
    suspend fun updateName(deviceId: Long, name: String): Device
    suspend fun updateDescription(deviceId: Long, description: String): Device
}