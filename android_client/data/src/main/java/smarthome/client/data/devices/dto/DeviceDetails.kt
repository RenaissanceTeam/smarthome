package smarthome.client.data.devices.dto

import smarthome.client.entity.Controller

data class DeviceDetails(
    val id: Long,
    val name: String,
    val description: String,
    val type: String,
    val controllers: List<Controller>
)