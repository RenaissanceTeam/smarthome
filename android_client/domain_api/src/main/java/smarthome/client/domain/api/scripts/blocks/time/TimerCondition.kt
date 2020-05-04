package smarthome.client.domain.api.scripts.blocks.time

import org.joda.time.Period
import smarthome.client.entity.script.dependency.condition.Condition

data class TimerCondition(
        override val id: String,
        val timer: Int = Period.minutes(5).seconds
) : Condition()