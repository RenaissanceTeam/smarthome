package smarthome.raspberry.scripts.api.domain.conditions

import org.joda.time.LocalTime
import smarthome.raspberry.entity.script.Condition
import javax.persistence.Embedded
import javax.persistence.Entity

@Entity
data class EachDayCondition(
        override val id: String,
        val time: LocalTime = LocalTime.MIDNIGHT
) : Condition(id)

