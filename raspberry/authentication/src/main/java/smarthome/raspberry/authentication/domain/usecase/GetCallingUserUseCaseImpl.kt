package smarthome.raspberry.authentication.domain.usecase

import org.springframework.stereotype.Component
import smarthome.raspberry.authentication.api.domain.entity.User
import smarthome.raspberry.authentication.api.domain.exceptions.NoAuthenticatedUserException
import smarthome.raspberry.authentication.api.domain.usecase.GetCallingUserUseCase
import smarthome.raspberry.authentication.data.AuthRepo
import smarthome.raspberry.authentication.data.security.SecurityRepository

@Component
class GetCallingUserUseCaseImpl(
        private val securityRepository: SecurityRepository,
        private val authRepo: AuthRepo
) : GetCallingUserUseCase {
    override fun execute(): User {
        val username = securityRepository.getAuthenticatedUsername()
        return authRepo.findByUsername(username) ?: throw NoAuthenticatedUserException()
    }
}