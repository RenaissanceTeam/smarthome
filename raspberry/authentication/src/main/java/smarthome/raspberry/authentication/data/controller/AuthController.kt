package smarthome.raspberry.authentication.data.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import smarthome.raspberry.authentication.api.domain.SignInUseCase
import smarthome.raspberry.authentication.api.domain.entity.Credentials

@RestController
class AuthController(
    private val signInUseCase: SignInUseCase
) {

    @PostMapping("/login")
    fun login(@RequestBody credentials: Credentials): Boolean {
        signInUseCase.execute(credentials)
        return true
    }
}