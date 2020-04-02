package smarthome.raspberry.arduinodevices.script.data.dto

import smarthome.raspberry.scripts.api.data.dto.ActionDto

class OnOffActionDto(
        id: String,
        val value: String
) : ActionDto(id)