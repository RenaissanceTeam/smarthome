package smarthome.raspberry.authentication.domain.usecase

import org.springframework.stereotype.Component
import smarthome.raspberry.authentication.api.domain.entity.User
import smarthome.raspberry.authentication.api.domain.usecase.GetUserByUsernameUseCase
import smarthome.raspberry.authentication.data.AuthRepo
import smarthome.raspberry.util.exceptions.notFound

@Component
class GetUserByUsernameUseCaseImpl(
        private val repo: AuthRepo
) : GetUserByUsernameUseCase {
    override fun execute(username: String): User {
        return repo.findByUsername(username) ?: throw notFound
    }
}