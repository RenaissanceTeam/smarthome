package smarthome.raspberry.notification.data.mapper

import com.google.gson.Gson
import okhttp3.RequestBody
import smarthome.raspberry.notification_api.domain.Notification

class NotificationToRequestBodyMapperImpl(
        private val gson: Gson
) : NotificationToRequestBodyMapper {
    override fun map(notification: Notification): RequestBody {
        val mediaType = okhttp3.MediaType.parse("application/json; charset=utf-8")
        return RequestBody.create(mediaType, gson.toJson(notification))
    }
}