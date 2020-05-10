package smarthome.client.data.auth

import smarthome.client.data.api.auth.SignUpCommand
import smarthome.client.data.auth.dto.Credentials
import smarthome.client.data.auth.dto.RegistrationInfo
import smarthome.client.data.retrofit.RetrofitFactory

class SignUpCommandImpl(
        private val retrofitFactory: RetrofitFactory
) : SignUpCommand {
    override suspend fun run(login: String, password: String, registrationCode: Long): String {
        return retrofitFactory.createApi(LoginApi::class.java)
                .signup(RegistrationInfo(
                        Credentials(login, password),
                        registrationCode
                )).token
    }
}