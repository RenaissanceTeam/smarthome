package smarthome.client.data.api.devices

import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.entity.Device

interface DevicesRepo {
    suspend fun getGeneralInfo(): List<GeneralDeviceInfo>
    suspend fun getById(deviceId: Long): Device
}