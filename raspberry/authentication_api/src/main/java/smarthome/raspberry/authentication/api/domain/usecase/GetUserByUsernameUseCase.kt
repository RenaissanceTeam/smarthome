package smarthome.raspberry.authentication.api.domain.usecase

import smarthome.raspberry.authentication.api.domain.entity.User

interface GetUserByUsernameUseCase {
    fun execute(username: String): User
}