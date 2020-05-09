package smarthome.client.domain.auth.usecases

import smarthome.client.data.api.auth.SignUpCommand
import smarthome.client.domain.api.auth.usecases.SaveNewTokenUseCase
import smarthome.client.domain.api.auth.usecases.SignUpUseCase

class SignUpUseCaseImpl(
        private val signUpCommand: SignUpCommand,
        private val saveNewTokenUseCase: SaveNewTokenUseCase
) : SignUpUseCase {

    override suspend fun execute(login: String, password: String, registrationCode: Long) {
        val token = signUpCommand.run(login, password, registrationCode)
        saveNewTokenUseCase.execute(token)
    }
}