package smarthome.raspberry.authentication.domain

import smarthome.raspberry.authentication_api.data.AuthRepo
import smarthome.raspberry.authentication_api.domain.GetUserIdUseCase

class GetUserIdUseCaseImpl(private val repo: AuthRepo) : GetUserIdUseCase {
    override fun execute() = repo.getUserId()
}