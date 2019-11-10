package smarthome.raspberry.authentication.domain

import smarthome.raspberry.authentication.api.data.AuthRepo
import smarthome.raspberry.authentication.api.domain.GetAuthStatusUseCase

class GetAuthStatusUseCaseImpl(private val repo: AuthRepo) : GetAuthStatusUseCase {
    override fun execute() = repo.getAuthStatus()
}
