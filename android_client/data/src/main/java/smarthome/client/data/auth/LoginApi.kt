package smarthome.client.data.auth

import retrofit2.http.Body
import retrofit2.http.POST
import smarthome.client.data.auth.dto.Credentials
import smarthome.client.data.auth.dto.RegistrationInfo
import smarthome.client.data.auth.dto.Token

interface LoginApi {
    @POST("/login")
    suspend fun login(@Body credentials: Credentials): Token

    @POST("/signup")
    suspend fun signup(@Body registrationInfo: RegistrationInfo): Token
}