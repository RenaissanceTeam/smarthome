package smarthome.raspberry.authentication.api.domain.entity

import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id

@Entity
data class UserRoles(
    @Id
    val username: String,
    @ElementCollection(targetClass = String::class, fetch = FetchType.EAGER)
    val roles: Set<String>
)