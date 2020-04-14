package smarthome.raspberry.entity.script

import smarthome.raspberry.entity.ID_NOT_DEFINED
import javax.persistence.*

@Entity
open class Script(
        @Id @GeneratedValue
        open val id: Long = ID_NOT_DEFINED,
        open val name: String,
        open val description: String,
        open val enabled: Boolean = true,
        @OneToMany(targetEntity = Block::class, cascade = [CascadeType.ALL])
        open val blocks: List<Block>,
        @OneToMany(targetEntity = Dependency::class, cascade = [CascadeType.ALL])
        open val dependencies: List<Dependency>
)

