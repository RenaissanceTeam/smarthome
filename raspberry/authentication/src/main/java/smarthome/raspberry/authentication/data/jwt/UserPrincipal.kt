package smarthome.raspberry.authentication.data.jwt

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import smarthome.raspberry.authentication.domain.entity.User
import smarthome.raspberry.authentication.domain.entity.UserRoles

class UserPrincipal(private val user: User, private val userRoles: UserRoles) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        userRoles.roles.map { SimpleGrantedAuthority("ROLE_$it") }.toMutableList()
    
    override fun isEnabled() = user.enabled
    
    override fun getUsername() = user.username
    
    override fun isCredentialsNonExpired() = true
    
    override fun getPassword() = user.password
    
    override fun isAccountNonExpired() = true
    
    override fun isAccountNonLocked() = true
}