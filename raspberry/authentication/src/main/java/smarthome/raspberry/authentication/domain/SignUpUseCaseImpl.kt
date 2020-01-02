package smarthome.raspberry.authentication.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.authentication.api.domain.SignUpUseCase
import smarthome.raspberry.authentication.api.domain.entity.Credentials
import smarthome.raspberry.authentication.api.domain.entity.RegistrationInfo
import smarthome.raspberry.authentication.data.AuthRepo

@Component
class SignUpUseCaseImpl(
    private val authRepo: AuthRepo
): SignUpUseCase {
    override fun execute(info: RegistrationInfo) {
        TODO()
    }
}