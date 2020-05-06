package smarthome.raspberry.scripts.api.domain.conditions

import smarthome.raspberry.entity.script.Condition
import javax.persistence.Entity

@Entity
data class TimerCondition(
        override val id: String,
        val timer: Int = 0
) : Condition(id)