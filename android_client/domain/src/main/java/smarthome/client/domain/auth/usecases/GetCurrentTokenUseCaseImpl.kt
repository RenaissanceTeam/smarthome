package smarthome.client.domain.auth.usecases

import smarthome.client.data.api.auth.TokenRepo
import smarthome.client.domain.api.auth.usecases.GetCurrentTokenUseCase

class GetCurrentTokenUseCaseImpl(
    private val tokenRepo: TokenRepo
) : GetCurrentTokenUseCase {
    override fun execute() = tokenRepo.getCurrent()
}