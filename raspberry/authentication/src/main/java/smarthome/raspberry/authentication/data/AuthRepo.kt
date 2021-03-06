package smarthome.raspberry.authentication.data

import org.springframework.data.jpa.repository.JpaRepository
import smarthome.raspberry.authentication.api.domain.entity.User

interface AuthRepo : JpaRepository<User, String> {
    fun findByUsername(username: String): User?
}