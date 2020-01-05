package smarthome.raspberry.authentication.data.jwt

import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
open class JwtTokenProvider {
    @Value("\${app.jwtSecret}")
    private lateinit var jwtSecret: String
    @Value("\${app.jwtExpirationInMs}")
    private val jwtExpirationInMs = 0
    
    fun generateToken(username: String): String {
        
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationInMs)
        
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }
    
    fun getUsernameFromToken(token: String?): String {
        return Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .body.subject
    }
    
    fun validateToken(authToken: String?): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken)
            return true
        } catch (ex: SignatureException) {
        } catch (ex: MalformedJwtException) {
        } catch (ex: ExpiredJwtException) {
        } catch (ex: UnsupportedJwtException) {
        } catch (ex: IllegalArgumentException) {
        }
        return false
    }
    
 
}