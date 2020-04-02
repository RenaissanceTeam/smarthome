package smarthome.raspberry.entity.script

import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
open class Dependency(
        @Id
        open val id: String,
        @ManyToOne
        open val start: Block,
        @ManyToOne
        open val end: Block,
        @OneToMany(targetEntity = Condition::class, cascade = [CascadeType.ALL])
        val conditions: List<Condition>,
        @OneToMany(targetEntity = Action::class, cascade = [CascadeType.ALL])
        val actions: List<Action>
)