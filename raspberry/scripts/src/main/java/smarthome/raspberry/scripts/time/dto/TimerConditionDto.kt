package smarthome.raspberry.scripts.time.dto

import smarthome.raspberry.scripts.api.data.dto.ConditionDto

class TimerConditionDto(
        id: String,
        val timer: Int
) : ConditionDto(id)