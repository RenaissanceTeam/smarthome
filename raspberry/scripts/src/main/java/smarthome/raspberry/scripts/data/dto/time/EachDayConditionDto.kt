package smarthome.raspberry.scripts.data.dto.time

import org.joda.time.LocalTime
import smarthome.raspberry.scripts.api.data.dto.ConditionDto

class EachDayConditionDto(
        id: String,
        val time: LocalTime
) : ConditionDto(id)