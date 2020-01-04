package smarthome.raspberry.authentication.domain.entity

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class UserRoles(
    @Id
    val username: String,
    val role: String
)