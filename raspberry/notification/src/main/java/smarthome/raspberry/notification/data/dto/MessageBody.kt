package smarthome.raspberry.notification.data.dto

import okhttp3.RequestBody
import smarthome.raspberry.notification_api.domain.Message
class MessageBody {
    companion object {
        fun fromMessage(message: Message) {
            TODO()
//            val mediaType = okhttp3.MediaType.parse("application/json; charset=utf-8")
//            return RequestBody.create(mediaType, gson.toJson(this))
        }
    }
}