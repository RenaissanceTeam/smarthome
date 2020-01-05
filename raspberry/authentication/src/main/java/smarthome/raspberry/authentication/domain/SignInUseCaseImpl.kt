package smarthome.raspberry.authentication.domain

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import smarthome.raspberry.authentication.api.domain.SignInUseCase
import smarthome.raspberry.authentication.api.domain.dto.TokenResponse
import smarthome.raspberry.authentication.api.domain.entity.Credentials
import smarthome.raspberry.authentication.data.jwt.JwtTokenProvider

@Component
class SignInUseCaseImpl(
    private val authenticationManager: AuthenticationManager,
    private val tokenProvider: JwtTokenProvider
): SignInUseCase {
    
    override fun execute(credentials: Credentials): TokenResponse {
        val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(
            credentials.login,
            credentials.password
        ))
        
        SecurityContextHolder.getContext().authentication = authentication
        
        val token = tokenProvider.generateToken(authentication)
        return TokenResponse(token)
    }
}

