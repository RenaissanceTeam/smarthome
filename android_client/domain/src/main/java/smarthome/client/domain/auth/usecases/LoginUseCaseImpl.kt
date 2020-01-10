package smarthome.client.domain.auth.usecases

import smarthome.client.data.api.auth.LoginCommand
import smarthome.client.domain.api.auth.usecases.LoginUseCase
import smarthome.client.domain.api.auth.usecases.SaveNewTokenUseCase

class LoginUseCaseImpl(
    private val loginCommand: LoginCommand,
    private val saveNewTokenUseCase: SaveNewTokenUseCase
) : LoginUseCase {
    override suspend fun execute(login: String, password: String) {
        val token = loginCommand.run(login, password)
        saveNewTokenUseCase.execute(token)
    }
}