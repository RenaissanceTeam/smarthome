package smarthome.raspberry.authentication.domain

import smarthome.raspberry.authentication.api.domain.GetAuthStatusUseCase
import smarthome.raspberry.authentication.data.AuthRepo

class GetAuthStatusUseCaseImpl(private val repo: AuthRepo) : GetAuthStatusUseCase {
    override fun execute() = repo.getAuthStatus()
}
