package smarthome.raspberry.arduinodevices.script.data.dto

import smarthome.raspberry.entity.ID_NOT_DEFINED
import smarthome.raspberry.scripts.api.data.dto.ConditionDto

class OnOffConditionDto(
        id: Long = ID_NOT_DEFINED,
        val value: String
) : ConditionDto(id)
