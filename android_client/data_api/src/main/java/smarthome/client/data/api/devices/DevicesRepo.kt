package smarthome.client.data.api.devices

import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo

interface DevicesRepo {
    suspend fun getGeneralInfo(): List<GeneralDeviceInfo>
}