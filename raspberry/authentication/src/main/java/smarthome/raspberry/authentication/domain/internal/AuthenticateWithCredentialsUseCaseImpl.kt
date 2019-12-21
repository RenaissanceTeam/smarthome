package smarthome.raspberry.authentication.domain.internal

import smarthome.raspberry.authentication.api.domain.Credentials
import smarthome.raspberry.authentication.api.domain.User
import smarthome.raspberry.authentication.data.AuthRepo
import smarthome.raspberry.home.api.domain.eventbus.PublishEventUseCase
import smarthome.raspberry.home.api.domain.eventbus.events.HasUser

class AuthenticateWithCredentialsUseCaseImpl(
        private val repo: AuthRepo,
        private val publishEventUseCase: PublishEventUseCase
) : AuthenticateWithCredentialsUseCase {
    
    override suspend fun execute(credential: Credentials): User {
        return repo.signIn(credential).also {
            publishEventUseCase.execute(HasUser())
        }
    }
}