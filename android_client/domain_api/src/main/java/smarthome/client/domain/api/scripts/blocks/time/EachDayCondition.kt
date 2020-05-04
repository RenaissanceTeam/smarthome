package smarthome.client.domain.api.scripts.blocks.time

import org.joda.time.LocalTime
import smarthome.client.entity.script.dependency.condition.Condition

data class EachDayCondition(override val id: String, val time: LocalTime = LocalTime.MIDNIGHT) : Condition()