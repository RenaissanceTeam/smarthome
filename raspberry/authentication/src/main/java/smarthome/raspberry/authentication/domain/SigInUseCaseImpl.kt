package smarthome.raspberry.authentication.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.authentication.api.domain.SignInUseCase
import smarthome.raspberry.authentication.api.domain.entity.Credentials

@Component
class SigInUseCaseImpl(): SignInUseCase {
    
    override fun execute(credentials: Credentials) {
        TODO()
    }
}