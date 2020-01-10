package smarthome.raspberry.devices.data.dto

import smarthome.raspberry.controllers.api.data.dto.GeneralControllerInfo

data class DeviceDetails(
        val id: Long,
        val serialName: String,
        val name: String,
        val description: String,
        val type: String,
        val controllers: List<GeneralControllerInfo>
)