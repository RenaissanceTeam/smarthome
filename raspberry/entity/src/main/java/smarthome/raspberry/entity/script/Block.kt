package smarthome.raspberry.entity.script

import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
open class Block(
        @Id
        val id: String,
        @Embedded
        val position: Position
)