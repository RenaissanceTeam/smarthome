package smarthome.raspberry.scripts.data.dto.time

import smarthome.raspberry.scripts.api.data.dto.ConditionDto

class TimerConditionDto(
        id: String,
        val timer: Int
) : ConditionDto(id)