package smarthome.raspberry.entity.script

import smarthome.raspberry.entity.ID_NOT_DEFINED
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
open class Condition(
        @Id @GeneratedValue
        open val id: Long = ID_NOT_DEFINED
)