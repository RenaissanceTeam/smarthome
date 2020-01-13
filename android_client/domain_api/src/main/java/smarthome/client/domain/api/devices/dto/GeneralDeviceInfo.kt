package smarthome.client.domain.api.devices.dto

import smarthome.client.entity.Controller

data class GeneralDeviceInfo(
    val id: Long,
    val name: String,
    val type: String,
    val controllers: List<Controller>
)

