package smarthome.raspberry.authentication.domain

import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.springframework.security.authentication.AuthenticationManager
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
        
        
        
    }
}

