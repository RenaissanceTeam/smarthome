package smarthome.raspberry.authentication.data.command

import smarthome.raspberry.authentication.api.domain.Credentials
import smarthome.raspberry.authentication.api.domain.User

interface SignInCommand {
    suspend fun execute(credential: Credentials): User
}