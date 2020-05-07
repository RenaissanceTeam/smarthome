package smarthome.raspberry.scripts.api.domain.notification

import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.script.Position
import javax.persistence.Entity

@Entity
data class NotificationBlock(
        override val id: String,
        override val position: Position
) : Block(id, position)