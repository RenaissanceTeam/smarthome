package smarthome.raspberry.entity.script

import smarthome.raspberry.entity.ID_NOT_DEFINED
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
open class Dependency(
        @Id @GeneratedValue
        open val id: Long = ID_NOT_DEFINED,
        @ManyToOne
        open val start: Block,
        @ManyToOne
        open val end: Block,
        @OneToMany(targetEntity = Condition::class, cascade = [CascadeType.ALL])
        val conditions: List<Condition>,
        @OneToMany(targetEntity = Action::class, cascade = [CascadeType.ALL])
        val actions: List<Action>
)