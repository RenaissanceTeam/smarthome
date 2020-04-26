package smarthome.raspberry.arduinodevices.script.data.dht

import smarthome.raspberry.scripts.api.data.dto.ConditionDto

class TemperatureConditionDto(id: String,
                              val sign: String,
                              val value: String) : ConditionDto(id)