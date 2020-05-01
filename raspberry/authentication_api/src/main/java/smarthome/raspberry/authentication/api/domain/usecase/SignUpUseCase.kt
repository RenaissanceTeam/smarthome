package smarthome.raspberry.authentication.api.domain.usecase

import smarthome.raspberry.authentication.api.domain.dto.TokenResponse
import smarthome.raspberry.authentication.api.domain.entity.RegistrationInfo

interface SignUpUseCase {
    fun execute(info: RegistrationInfo): TokenResponse
}