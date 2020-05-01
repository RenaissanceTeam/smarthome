package smarthome.client.data.notifications.api

import retrofit2.http.Body
import retrofit2.http.POST
import smarthome.client.data.notifications.dto.TokenDto

interface NotificationApi {

    @POST("api/notifications")
    suspend fun save(@Body token: TokenDto)
}