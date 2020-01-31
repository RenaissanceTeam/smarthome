package smarthome.raspberry.authentication.api.domain

import smarthome.raspberry.authentication.api.domain.dto.TokenResponse
import smarthome.raspberry.authentication.api.domain.entity.Credentials

interface SignInUseCase {
    fun execute(credentials: Credentials): TokenResponse
}