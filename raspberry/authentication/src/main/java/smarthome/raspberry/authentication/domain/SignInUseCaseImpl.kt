package smarthome.raspberry.authentication.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.authentication.api.domain.SignInUseCase
import smarthome.raspberry.authentication.api.domain.dto.TokenResponse
import smarthome.raspberry.authentication.api.domain.entity.Credentials
import smarthome.raspberry.authentication.data.AuthenticatorAdapter
import smarthome.raspberry.authentication.data.jwt.JwtTokenProvider

@Component
class SignInUseCaseImpl(
    private val authenticatorAdapter: AuthenticatorAdapter,
    private val tokenProvider: JwtTokenProvider
): SignInUseCase {
    
    override fun execute(credentials: Credentials): TokenResponse {
        authenticatorAdapter.authenticate(credentials)
        
        val token = tokenProvider.generateToken(credentials.login)
        return TokenResponse(token)
    }
}

