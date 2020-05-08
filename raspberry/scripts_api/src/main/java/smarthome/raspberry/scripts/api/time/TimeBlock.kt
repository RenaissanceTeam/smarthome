package smarthome.raspberry.scripts.api.time

import org.joda.time.LocalTime
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.script.Position
import javax.persistence.Entity

@Entity
data class TimeBlock(
        override val id: String,
        override val position: Position,
        val time: LocalTime? = null
) : Block(id, position)
