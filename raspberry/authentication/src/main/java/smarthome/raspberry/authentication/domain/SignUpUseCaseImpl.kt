package smarthome.raspberry.authentication.domain

import smarthome.raspberry.authentication.api.domain.SignUpUseCase
import smarthome.raspberry.authentication.api.domain.entity.Credentials
import smarthome.raspberry.authentication.data.AuthRepo

class SignUpUseCaseImpl(
    private val authRepo: AuthRepo
): SignUpUseCase {
    override fun execute(credentials: Credentials) {
        TODO()
    }
}