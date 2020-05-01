package smarthome.raspberry.authentication.data

import org.springframework.data.jpa.repository.JpaRepository
import smarthome.raspberry.authentication.api.domain.entity.UserRoles

interface UserRoleRepo : JpaRepository<UserRoles, String> {
    fun findByUsername(username: String): UserRoles
}