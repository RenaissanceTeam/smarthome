package smarthome.raspberry.arduinodevices.script.data.analog

import smarthome.raspberry.scripts.api.data.dto.ConditionDto

class AnalogConditionDto(
        id: String,
        val value: String
) : ConditionDto(id)

