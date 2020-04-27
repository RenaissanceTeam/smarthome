package smarthome.raspberry.arduinodevices.script.data.digital

import smarthome.raspberry.scripts.api.data.dto.ConditionDto

class DigitalConditionDto(
        id: String,
        val value: String
) : ConditionDto(id)

