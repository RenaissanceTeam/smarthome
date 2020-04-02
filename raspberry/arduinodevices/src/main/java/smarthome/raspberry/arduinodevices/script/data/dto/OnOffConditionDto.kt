package smarthome.raspberry.arduinodevices.script.data.dto

import smarthome.raspberry.scripts.api.data.dto.ConditionDto

class OnOffConditionDto(
        id: String,
        val value: String
) : ConditionDto(id)
