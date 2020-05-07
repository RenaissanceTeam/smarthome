package smarthome.raspberry.scripts.data.dto.location

import smarthome.raspberry.scripts.api.data.dto.ConditionDto

class HomeGeofenceConditionDto(
        id: String,
        val inside: Boolean
) : ConditionDto(id)