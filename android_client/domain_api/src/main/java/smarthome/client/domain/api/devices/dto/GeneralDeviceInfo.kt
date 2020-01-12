package smarthome.client.domain.api.devices.dto

import smarthome.client.domain.api.conrollers.dto.GeneralControllerInfo

data class GeneralDeviceInfo(
    val id: Long,
    val name: String,
    val type: String,
    val controllers: List<GeneralControllerInfo>
)

