package smarthome.raspberry.authentication.data.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
open class SecurityRepositoryImpl : SecurityRepository {
    override fun getAuthenticatedUsername(): String {
        return SecurityContextHolder.getContext().authentication.name
    }
}
