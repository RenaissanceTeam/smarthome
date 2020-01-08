package smarthome.client.domain.usecases

import smarthome.client.data.api.home.HomeRepository
import smarthome.client.domain.api.usecase.CloudMessageUseCase

class CloudMessageUseCaseImpl(private val homeRepository: HomeRepository) : CloudMessageUseCase {
    override suspend fun onNewToken(newToken: String?) {
        newToken ?: return

        homeRepository.saveAppToken(newToken)
    }
    
    override suspend fun noSavedToken(): Boolean {
        val token = homeRepository.getSavedAppToken()

        return token == null
    }
}
