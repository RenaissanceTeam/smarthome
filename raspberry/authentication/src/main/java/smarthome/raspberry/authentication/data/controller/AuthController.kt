package smarthome.raspberry.authentication.data.controller

import org.koin.core.KoinComponent
import org.koin.core.inject
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import smarthome.raspberry.authentication.api.domain.SignUpUseCase
import smarthome.raspberry.authentication.api.domain.entity.Credentials

@RestController
class AuthController : KoinComponent {
    private val signUpUseCase by inject<SignUpUseCase>()

    //    @PostMapping("/signup")
//    fun signUp(credentials: Credentials) {
//
//    }
//
    @PostMapping("/login")
    fun login(credentials: Credentials) {
    
    }
}