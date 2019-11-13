package smarthome.raspberry.notification.data.mapper

import okhttp3.RequestBody
import smarthome.raspberry.notification.api.domain.Notification

interface NotificationToRequestBodyMapper {
    fun map(notification: Notification): RequestBody
}