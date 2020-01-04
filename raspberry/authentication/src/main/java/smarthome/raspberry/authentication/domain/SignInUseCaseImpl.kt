package smarthome.raspberry.authentication.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.authentication.api.domain.SignInUseCase
import smarthome.raspberry.authentication.api.domain.entity.Credentials
import smarthome.raspberry.authentication.api.domain.exceptions.UserExistsException
import smarthome.raspberry.authentication.data.AuthRepo

@Component
class SignInUseCaseImpl(
    private val authRepo: AuthRepo
): SignInUseCase {
    
    override fun execute(credentials: Credentials) {
        val user = authRepo.findByUsername(credentials.login)
        
        if (user != null) {
            throw UserExistsException()
        }
    }
}