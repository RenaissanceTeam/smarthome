package smarthome.client.domain.usecases

import io.reactivex.Observable
import smarthome.client.data.api.auth.UserRepository
import smarthome.client.domain.api.usecase.AuthenticationUseCase

class AuthenticationUseCaseImpl(
    private val repository: UserRepository
) : AuthenticationUseCase {
    override fun getAuthenticationStatus(): Observable<Boolean> = TODO()
    
    override fun getUsername(): Observable<String> {
        return repository.get().map { it.username }
    }
    
    override fun signOut() {
        repository.delete()
    }
}
