package smarthome.raspberry.authentication.data.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import smarthome.raspberry.authentication.api.domain.SignInUseCase
import smarthome.raspberry.authentication.api.domain.SignUpUseCase
import smarthome.raspberry.authentication.api.domain.entity.Credentials
import smarthome.raspberry.authentication.api.domain.entity.RegistrationInfo
import smarthome.raspberry.authentication.api.domain.exceptions.UserExistsException
import smarthome.raspberry.authentication.domain.entity.Roles


@RestController
class AuthController(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase
) {

    @PostMapping("/login")
    fun login(@RequestBody credentials: Credentials): Boolean {
        try {
            signInUseCase.execute(credentials)
        } catch (u: UserExistsException) {
        
        }
        return true
    }
    
    @PostMapping("/signup")
    fun signUp(@RequestBody credentials: Credentials) {
        try {
            signUpUseCase.execute(RegistrationInfo(credentials, Roles.USER.name))
        } catch (u: UserExistsException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "User ${credentials.login} already exists")
        }
    }
    
    @PostMapping("/signup/admin")
    fun signUpAdmin(@RequestBody credentials: Credentials): Boolean {
        signUpUseCase.execute(RegistrationInfo(credentials, Roles.ADMIN.name))
        return true
    }
    
    
    @GetMapping("/api/s")
    fun te() {
        
    }
}