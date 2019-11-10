package smarthome.raspberry.authentication.domain

import smarthome.raspberry.authentication.api.domain.GetUserInfoUseCase
import smarthome.raspberry.authentication.api.domain.User
import smarthome.raspberry.authentication.data.AuthRepo

class GetUserInfoUseCaseImpl(
        private val authRepo: AuthRepo
): GetUserInfoUseCase {
    override fun execute() = authRepo.getUser()
}