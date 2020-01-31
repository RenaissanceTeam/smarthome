package smarthome.client.data.auth

import smarthome.client.data.api.auth.LoginCommand
import smarthome.client.data.auth.dto.Credentials
import smarthome.client.data.retrofit.RetrofitFactory

class LoginCommandImpl(
    private val retrofit: RetrofitFactory
) : LoginCommand {
    override suspend fun run(login: String, password: String): String {
        return retrofit.getInstance().create(LoginApi::class.java) // todo optimize (add cache)
            .login(Credentials(login, password)).token
    }
}
