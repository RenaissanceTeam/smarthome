package smarthome.raspberry.scripts.api.domain.actions

import smarthome.raspberry.authentication.api.domain.entity.User
import smarthome.raspberry.entity.script.Action
import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
data class SendNotificationAction(
        override val id: String,
        @ManyToOne
        val user: User,
        val message: String
) : Action(id)