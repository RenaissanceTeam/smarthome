package smarthome.raspberry.authentication.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.authentication.api.domain.SignUpUseCase
import smarthome.raspberry.authentication.api.domain.entity.RegistrationInfo
import smarthome.raspberry.authentication.api.domain.exceptions.UserExistsException
import smarthome.raspberry.authentication.data.AuthRepo
import smarthome.raspberry.authentication.data.UserRoleRepo
import smarthome.raspberry.authentication.domain.entity.User
import smarthome.raspberry.authentication.domain.entity.UserRole

@Component
class SignUpUseCaseImpl(
    private val authRepo: AuthRepo,
    private val roleRepo: UserRoleRepo
): SignUpUseCase {
    
    override fun execute(info: RegistrationInfo) {
        if (authRepo.findByUsername(info.credentials.login) != null) throw UserExistsException()
        
        val user = User(info.credentials.login, info.credentials.password, true)
        val userRole = UserRole(user.username, info.role)
        
        authRepo.save(user)
        roleRepo.save(userRole)
    }
}