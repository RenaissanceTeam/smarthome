package smarthome.client.domain.auth.usecases

import smarthome.client.data.api.auth.TokenRepo
import smarthome.client.domain.api.auth.usecases.LogoutUseCase

class LogoutUseCaseImpl(
        private val tokenRepo: TokenRepo
) : LogoutUseCase {

    override fun execute() {
        tokenRepo.clear()
    }
}