package smarthome.raspberry.scripts.location.dto

import smarthome.raspberry.scripts.api.data.dto.ConditionDto

class HomeGeofenceConditionDto(
        id: String,
        val inside: Boolean
) : ConditionDto(id)