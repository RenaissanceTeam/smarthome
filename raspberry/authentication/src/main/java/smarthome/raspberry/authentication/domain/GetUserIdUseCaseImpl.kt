package smarthome.raspberry.authentication.domain

import smarthome.raspberry.authentication.api.domain.GetUserIdUseCase
import smarthome.raspberry.authentication.data.AuthRepo

class GetUserIdUseCaseImpl(private val repo: AuthRepo) : GetUserIdUseCase {
    override fun execute() = repo.getUserId()
}