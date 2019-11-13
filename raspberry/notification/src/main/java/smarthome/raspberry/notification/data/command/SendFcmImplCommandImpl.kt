package smarthome.raspberry.notification.data.command

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import smarthome.raspberry.notification.data.FcmSenderApi
import smarthome.raspberry.notification.data.mapper.NotificationToRequestBodyMapper
import smarthome.raspberry.notification.api.domain.Notification

class SendFcmImplCommandImpl(
        private val fcmSenderApi: FcmSenderApi,
        private val notificationToRequestBodyMapper: NotificationToRequestBodyMapper
) : SendFcmCommand{
    // todo should be provided from di
//    private val api: FcmSenderApi = Retrofit.Builder()
//        .baseUrl("https://us-central1-smarthome-d2238.cloudfunctions.net/")
//        .build()
//        .create(FcmSenderApi::class.java)
//
    override suspend fun send(notification: Notification) {
        withContext(Dispatchers.IO) {
            fcmSenderApi.send(notificationToRequestBodyMapper.map(notification)).execute()
        }
    }
}