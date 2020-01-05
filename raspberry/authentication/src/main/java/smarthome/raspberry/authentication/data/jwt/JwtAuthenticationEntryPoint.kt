package smarthome.raspberry.authentication.data.jwt

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
open class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {
    
    override fun commence(httpServletRequest: HttpServletRequest?,
                          httpServletResponse: HttpServletResponse,
                          e: AuthenticationException) {
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.message)
    }
}