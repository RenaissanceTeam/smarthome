package smarthome.raspberry.authentication.domain

import smarthome.raspberry.authentication_api.data.AuthRepo
import smarthome.raspberry.authentication_api.domain.GetAuthStatusUseCase

class GetAuthStatusUseCaseImpl(private val repo: AuthRepo) : GetAuthStatusUseCase {
    override fun execute() = repo.getAuthStatus()
}

