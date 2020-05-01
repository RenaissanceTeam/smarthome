package smarthome.raspberry.authentication.data.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import smarthome.raspberry.authentication.api.domain.usecase.SignInUseCase
import smarthome.raspberry.authentication.api.domain.usecase.SignUpUseCase
import smarthome.raspberry.authentication.api.domain.dto.TokenResponse
import smarthome.raspberry.authentication.api.domain.entity.Credentials
import smarthome.raspberry.authentication.api.domain.entity.RegistrationInfo
import smarthome.raspberry.authentication.api.domain.exceptions.UserExistsException
import smarthome.raspberry.authentication.api.domain.entity.Roles


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
    fun signUp(@RequestBody credentials: Credentials): TokenResponse {
        try {
            return signUpUseCase.execute(RegistrationInfo(credentials, setOf(Roles.USER.name)))
        } catch (u: UserExistsException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "User ${credentials.login} already exists")
        }
    }
    
    @PostMapping("/signup/admin")
    fun signUpAdmin(@RequestBody credentials: Credentials): TokenResponse {
        try {
            return signUpUseCase.execute(RegistrationInfo(credentials, setOf(Roles.USER.name, Roles.ADMIN.name)))
        } catch (u: UserExistsException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "User ${credentials.login} already exists")
        }
    }
    
    
    @GetMapping("/api/s")
    fun te() {
    
    }
}