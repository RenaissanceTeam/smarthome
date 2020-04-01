package smarthome.raspberry.entity.script

import smarthome.raspberry.entity.ID_NOT_DEFINED
import javax.persistence.*

@Entity
open class Script(
        @Id @GeneratedValue
        val id: Long = ID_NOT_DEFINED,
        val name: String,
        val description: String,
        val enabled: Boolean = true,
        @OneToMany(targetEntity = Block::class, cascade = [CascadeType.ALL])
        val blocks: List<Block>,
        @OneToMany(targetEntity = Dependency::class, cascade = [CascadeType.ALL])
        val dependencies: List<Dependency>
)

