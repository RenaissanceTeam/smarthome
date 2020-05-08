package smarthome.raspberry.authentication.domain.usecase

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import smarthome.raspberry.authentication.api.domain.dto.TokenResponse
import smarthome.raspberry.authentication.api.domain.entity.Credentials
import smarthome.raspberry.authentication.api.domain.entity.Roles
import smarthome.raspberry.authentication.api.domain.entity.User
import smarthome.raspberry.authentication.api.domain.entity.UserRoles
import smarthome.raspberry.authentication.api.domain.exceptions.IllegalRegistrationCode
import smarthome.raspberry.authentication.api.domain.exceptions.UserExistsException
import smarthome.raspberry.authentication.api.domain.usecase.SignInUseCase
import smarthome.raspberry.authentication.api.domain.usecase.SignUpUseCase
import smarthome.raspberry.authentication.data.AuthRepo
import smarthome.raspberry.authentication.data.UserRoleRepo

@Component
class SignUpUseCaseImpl(
        private val authRepo: AuthRepo,
        private val roleRepo: UserRoleRepo,
        private val signInUseCase: SignInUseCase
) : SignUpUseCase {
    @Value("\${registrationCode}")
    private lateinit var registrationCode: String


    override fun execute(credentials: Credentials, registrationCode: Long): TokenResponse {
        if (authRepo.findByUsername(credentials.login) != null) throw UserExistsException()
        if (registrationCode != this.registrationCode.toLong()) throw IllegalRegistrationCode()

        val user = User(credentials.login, credentials.password, true)
        val userRole = UserRoles(user.username, setOf(Roles.ADMIN.name, Roles.USER.name))

        authRepo.save(user)
        roleRepo.save(userRole)

        return signInUseCase.execute(credentials)
    }

}