package smarthome.raspberry.scripts.api.domain.conditions

import org.joda.time.LocalTime
import smarthome.raspberry.entity.script.Condition
import javax.persistence.ElementCollection
import javax.persistence.Embedded
import javax.persistence.Entity

@Entity
data class WeekdaysCondition(
        override val id: String,
        @ElementCollection
        val days: List<Int> = listOf(),
        val time: LocalTime = LocalTime.MIDNIGHT
) : Condition(id)
