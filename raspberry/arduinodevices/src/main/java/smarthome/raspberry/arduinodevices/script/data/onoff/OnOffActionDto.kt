package smarthome.raspberry.arduinodevices.script.data.onoff

import smarthome.raspberry.scripts.api.data.dto.ActionDto

class OnOffActionDto(
        id: String,
        val value: String
) : ActionDto(id)