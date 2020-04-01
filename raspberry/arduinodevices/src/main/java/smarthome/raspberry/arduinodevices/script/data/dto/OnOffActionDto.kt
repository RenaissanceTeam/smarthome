package smarthome.raspberry.arduinodevices.script.data.dto

import smarthome.raspberry.entity.ID_NOT_DEFINED
import smarthome.raspberry.scripts.api.data.dto.ActionDto

class OnOffActionDto(
        id: Long = ID_NOT_DEFINED,
        val value: String
) : ActionDto(id)