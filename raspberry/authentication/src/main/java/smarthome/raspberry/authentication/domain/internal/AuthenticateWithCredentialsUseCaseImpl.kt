package smarthome.raspberry.authentication.domain.internal

import smarthome.raspberry.authentication.api.domain.Credentials
import smarthome.raspberry.authentication.api.domain.User
import smarthome.raspberry.authentication.data.AuthRepo

class AuthenticateWithCredentialsUseCaseImpl(
        private val repo: AuthRepo
) : AuthenticateWithCredentialsUseCase {
    
    override suspend fun execute(credential: Credentials): User {
        return repo.signIn(credential)
    }
}