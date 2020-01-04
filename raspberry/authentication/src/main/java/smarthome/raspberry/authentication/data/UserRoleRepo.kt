package smarthome.raspberry.authentication.data

import org.springframework.data.jpa.repository.JpaRepository
import smarthome.raspberry.authentication.domain.entity.UserRole

interface UserRoleRepo : JpaRepository<UserRole, String>