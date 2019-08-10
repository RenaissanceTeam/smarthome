package smarthome.raspberry.data.remote.fcm

import com.google.gson.Gson
import okhttp3.RequestBody


class MessageBody(
        val title: String,
        val body: String,
        val tokens: Array<String>,
        val type: String,
        val priority: String) {

    companion object {
        private val gson = Gson()
    }

    fun toRequestBody(): RequestBody {
        val mediaType = okhttp3.MediaType.parse("application/json; charset=utf-8")
        return RequestBody.create(mediaType, gson.toJson(this))
    }
}

