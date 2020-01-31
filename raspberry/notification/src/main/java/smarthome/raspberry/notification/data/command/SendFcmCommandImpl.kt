package smarthome.raspberry.notification.data.command

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import smarthome.raspberry.notification.api.domain.Notification
import smarthome.raspberry.notification.data.FcmSenderApi
import smarthome.raspberry.notification.data.mapper.NotificationToRequestBodyMapper

class SendFcmCommandImpl(
        private val fcmSenderApi: FcmSenderApi,
        private val notificationToRequestBodyMapper: NotificationToRequestBodyMapper
) : SendFcmCommand{

    override suspend fun send(notification: Notification) {
        withContext(Dispatchers.IO) {
            fcmSenderApi.send(notificationToRequestBodyMapper.map(notification)).execute()
        }
    }
}