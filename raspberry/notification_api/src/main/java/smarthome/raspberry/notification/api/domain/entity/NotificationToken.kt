package smarthome.raspberry.notification.api.domain.entity

import smarthome.raspberry.authentication.api.domain.entity.User
import smarthome.raspberry.entity.ID_NOT_DEFINED
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
data class NotificationToken(
        @Id @GeneratedValue
        val id: Long = ID_NOT_DEFINED,
        @OneToOne
        val user: User,
        val token: String = ""
)