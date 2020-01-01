package smarthome.raspberry.authentication.api.domain

import smarthome.raspberry.authentication.api.domain.entity.Credentials

interface SignUpUseCase {
    fun execute(credentials: Credentials)
}