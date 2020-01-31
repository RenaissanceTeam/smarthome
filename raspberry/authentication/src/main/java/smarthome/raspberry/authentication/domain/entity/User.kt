package smarthome.raspberry.authentication.domain.entity

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "usr")
data class User(
    
    @Id
    val username: String,
    val password: String,
    val enabled: Boolean
)
