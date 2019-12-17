package smarthome.raspberry.authentication.domain.internal

import smarthome.raspberry.authentication.api.domain.Credentials
import smarthome.raspberry.authentication.api.domain.User

interface AuthenticateWithCredentialsUseCase {
    suspend fun execute(credential: Credentials): User
}

