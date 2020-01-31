package smarthome.client.domain.auth.usecases

import smarthome.client.data.api.auth.TokenRepo
import smarthome.client.domain.api.auth.usecases.SaveNewTokenUseCase

class SaveNewTokenUseCaseImpl(
    private val tokenRepo: TokenRepo
) : SaveNewTokenUseCase {
    override fun execute(token: String) {
        tokenRepo.save(token)
    }
}