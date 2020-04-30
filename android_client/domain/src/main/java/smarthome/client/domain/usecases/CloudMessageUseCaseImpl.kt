package smarthome.client.domain.usecases

import smarthome.client.domain.api.usecase.CloudMessageUseCase

class CloudMessageUseCaseImpl() : CloudMessageUseCase {
    override suspend fun onNewToken(newToken: String?) {
        newToken ?: return

//        TODO()
    }

    override suspend fun noSavedToken(): Boolean {
//        TODO()
        return false
    }
}
