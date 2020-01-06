package smarthome.raspberry.authentication.data

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import smarthome.raspberry.authentication.api.domain.entity.Credentials

@Component
open class SpringAuthenticator(private val authenticationManager: AuthenticationManager):
    AuthenticatorAdapter {
    override fun authenticate(credentials: Credentials) {
        val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(
            credentials.login,
            credentials.password
        ))
    
        SecurityContextHolder.getContext().authentication = authentication
    }
}