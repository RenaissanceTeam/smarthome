package smarthome.raspberry.authentication.domain.usecase

import org.springframework.stereotype.Component
import smarthome.raspberry.authentication.api.domain.usecase.SignInUseCase
import smarthome.raspberry.authentication.api.domain.usecase.SignUpUseCase
import smarthome.raspberry.authentication.api.domain.dto.TokenResponse
import smarthome.raspberry.authentication.api.domain.entity.RegistrationInfo
import smarthome.raspberry.authentication.api.domain.exceptions.UserExistsException
import smarthome.raspberry.authentication.data.AuthRepo
import smarthome.raspberry.authentication.data.UserRoleRepo
import smarthome.raspberry.authentication.api.domain.entity.User
import smarthome.raspberry.authentication.api.domain.entity.UserRoles

@Component
class SignUpUseCaseImpl(
    private val authRepo: AuthRepo,
    private val roleRepo: UserRoleRepo,
    private val signInUseCase: SignInUseCase
): SignUpUseCase {
    
    override fun execute(info: RegistrationInfo): TokenResponse {
        if (authRepo.findByUsername(info.credentials.login) != null) throw UserExistsException()
        
        val user = User(info.credentials.login, info.credentials.password, true)
        val userRole = UserRoles(user.username, info.roles)
        
        authRepo.save(user)
        roleRepo.save(userRole)
        
        return signInUseCase.execute(info.credentials)
    }
}