package smarthome.raspberry.notification.data.mapper

import okhttp3.RequestBody
import smarthome.raspberry.notification_api.domain.Notification

interface NotificationToRequestBodyMapper {
    fun map(notification: Notification): RequestBody
}