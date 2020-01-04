package smarthome.raspberry.authentication.api.domain

import smarthome.raspberry.authentication.api.domain.entity.RegistrationInfo

interface SignUpUseCase {
    fun execute(info: RegistrationInfo)
}