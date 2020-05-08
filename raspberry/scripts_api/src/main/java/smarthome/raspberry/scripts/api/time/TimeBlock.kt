package smarthome.raspberry.scripts.api.time

import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.script.Position
import javax.persistence.Entity

@Entity
data class TimeBlock(override val id: String, override val position: Position) : Block(id, position)
