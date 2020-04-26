package smarthome.raspberry.arduinodevices.script.data.onoff

import smarthome.raspberry.scripts.api.data.dto.ConditionDto

class OnOffConditionDto(
        id: String,
        val value: String
) : ConditionDto(id)
