package smarthome.client.data.devices.dto

import smarthome.client.entity.Controller

data class GeneralDeviceAndControllersInfo(
    val id: Long,
    val name: String,
    val type: String,
    val controllers: List<Controller>
)