package smarthome.raspberry.scripts.data.dto.time

import org.joda.time.LocalTime
import smarthome.raspberry.scripts.api.data.dto.ConditionDto

class WeekdaysConditionDto(
        id: String,
        val days: List<Int>,
        val time: LocalTime = LocalTime.MIDNIGHT
) : ConditionDto(id)

