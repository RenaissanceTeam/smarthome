package smarthome.raspberry.devices.api.data.dto

import smarthome.raspberry.controllers.api.data.dto.GeneralControllerInfo

data class DeviceDetails(
        val id: Long,
        val serial: Int,
        val name: String,
        val description: String,
        val type: String,
        val controllers: List<GeneralControllerInfo>
)