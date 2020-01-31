package smarthome.raspberry.devices.api.data.dto

import smarthome.raspberry.controllers.api.data.dto.GeneralControllerInfo

data class GeneralDeviceInfo(
        val id: Long,
        val name: String,
        val type: String,
        val controllers: List<GeneralControllerInfo>
)

