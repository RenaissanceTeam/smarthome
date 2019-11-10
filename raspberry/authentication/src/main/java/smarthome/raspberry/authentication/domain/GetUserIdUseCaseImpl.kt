package smarthome.raspberry.authentication.domain

import smarthome.raspberry.authentication.api.data.AuthRepo
import smarthome.raspberry.authentication.api.domain.GetUserIdUseCase

class GetUserIdUseCaseImpl(private val repo: AuthRepo) : GetUserIdUseCase {
    override fun execute() = repo.getUserId()
}