package smarthome.raspberry.entity.script

import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
open class Block(
        @Id
        open val id: String,
        @Embedded
        open val position: Position
)