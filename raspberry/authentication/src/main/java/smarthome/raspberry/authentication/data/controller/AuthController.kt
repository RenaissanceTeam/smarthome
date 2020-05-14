package smarthome.raspberry.authentication.data.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import smarthome.raspberry.authentication.api.domain.dto.TokenResponse
import smarthome.raspberry.authentication.api.domain.entity.Credentials
import smarthome.raspberry.authentication.api.domain.usecase.SignInUseCase
import smarthome.raspberry.authentication.api.domain.usecase.SignUpUseCase
import smarthome.raspberry.authentication.data.dto.SignUpInfo


@RestController
class AuthController(
        private val signInUseCase: SignInUseCase,
        private val signUpUseCase: SignUpUseCase
) {

    @PostMapping("/login")
    fun login(@RequestBody credentials: Credentials): TokenResponse {
        return signInUseCase.execute(credentials)
    }

    @PostMapping("/signup")
    fun signUp(@RequestBody signUpInfo: SignUpInfo): TokenResponse {
        return signUpUseCase.execute(signUpInfo.credentials, signUpInfo.registrationCode)
    }
}