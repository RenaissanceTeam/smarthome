package smarthome.client.data.auth

import smarthome.client.data.api.auth.LoginCommand
import smarthome.client.data.auth.dto.Credentials

class LoginCommandImpl(
    private val loginApi: LoginApi
) : LoginCommand {
    override suspend fun run(login: String, password: String): String {
        return loginApi.login(Credentials(login, password)).token
    }
}


