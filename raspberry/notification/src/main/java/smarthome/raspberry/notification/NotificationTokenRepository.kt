package smarthome.raspberry.notification

import org.springframework.data.jpa.repository.JpaRepository
import smarthome.raspberry.notification.api.domain.entity.NotificationToken

interface NotificationTokenRepository : JpaRepository<NotificationToken, String> {
    fun findByUserUsername(username: String): NotificationToken?
}