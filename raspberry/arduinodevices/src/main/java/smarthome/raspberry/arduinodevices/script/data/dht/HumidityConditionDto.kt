package smarthome.raspberry.arduinodevices.script.data.dht

import smarthome.raspberry.scripts.api.data.dto.ConditionDto

class HumidityConditionDto(id: String,
                           val sign: String,
                           val value: String
) : ConditionDto(id)
