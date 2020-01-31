package smarthome.raspberry.authentication.data.jwt

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import smarthome.raspberry.authentication.data.AuthRepo
import smarthome.raspberry.authentication.data.UserRoleRepo


@Service
open class CustomUserDetailsService(
    private val authRepo: AuthRepo,
    private val userRoleRepo: UserRoleRepo
) : UserDetailsService {
    
    
    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val user = authRepo.findByUsername(username) ?: throw UsernameNotFoundException(
            "Not found $username")
        val roles = userRoleRepo.findByUsername(username)
        
        return UserPrincipal(user, roles)
    }
}