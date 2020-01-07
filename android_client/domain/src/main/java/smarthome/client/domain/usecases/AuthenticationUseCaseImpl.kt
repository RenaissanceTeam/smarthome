package smarthome.client.domain.usecases

import smarthome.client.data_api.auth.AuthenticationRepository
import smarthome.client.domain.api.usecase.AuthenticationUseCase

class AuthenticationUseCaseImpl(
    private val repository: AuthenticationRepository
) : AuthenticationUseCase {
    override fun getAuthenticationStatus() = repository.getAuthenticationStatus()
    override fun getEmail() = repository.getEmail()
    override suspend fun onAuthFail() {
        repository.setAuthenticationStatus(false)
        repository.resetEmail()
    }
    override suspend fun onAuthSuccess() {
        repository.setAuthenticationStatus(true)
        repository.updateEmail()
    }
    override suspend fun signOut() {
        repository.setAuthenticationStatus(false)
        repository.resetEmail()
    }
    override suspend fun getUserId(): String? {
        return repository.getUserId()
    }
}
