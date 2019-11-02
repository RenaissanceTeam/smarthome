package smarthome.client.domain.usecases

import smarthome.client.data_api.HomeRepository

class CloudMessageUseCase(private val homeRepository: HomeRepository) {
    suspend fun onNewToken(newToken: String?) {
        newToken ?: return

        homeRepository.saveAppToken(newToken)
    }

    suspend fun noSavedToken(): Boolean {
        val token = homeRepository.getSavedAppToken()

        return token == null
    }
}
