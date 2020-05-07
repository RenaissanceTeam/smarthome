package smarthome.raspberry.scripts.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.core.toOptional
import smarthome.raspberry.entity.script.*
import smarthome.raspberry.scripts.api.domain.ActionRunner
import smarthome.raspberry.scripts.api.domain.ConditionValidator
import smarthome.raspberry.scripts.api.domain.BlockObserver
import smarthome.raspberry.scripts.domain.usecase.RegisterScriptProtocolUseCaseImpl
import java.util.*

class RegisterScriptProtocolUseCaseImplTest {

    private lateinit var protocol: RegisterScriptProtocolUseCaseImpl
    private lateinit var blockObserver: BlockObserver
    private lateinit var blockObservers: Map<String, BlockObserver>
    private lateinit var conditionValidators: Map<String, ConditionValidator>
    private lateinit var actionRunner: ActionRunner
    private lateinit var actionRunners: Map<String, ActionRunner>
    private lateinit var script: Script
    private lateinit var block_a: Block
    private lateinit var block_b: Block
    private lateinit var dependency_ab: Dependency
    private lateinit var condition_a: Condition
    private lateinit var action_b: Action
    private lateinit var validator: ConditionValidator

    @Before
    fun setUp() {

        block_a = Block("a", Position(1, 1))
        block_b = Block("b", Position(1, 1))

        condition_a = Condition("condition_a")
        action_b = Action("action_b")

        blockObserver =  mock { }
        blockObservers = mapOf(
                "blockObserver" to blockObserver
        )
        actionRunner = mock { }
        actionRunners = mapOf("actionRunner" to  actionRunner)

        validator = mock { }
        conditionValidators = mapOf("conditionValidator" to validator)
        script = Script(
                name = "",
                description = "",
                enabled = true,
                blocks = listOf(),
                dependencies = listOf()
        )
        protocol = RegisterScriptProtocolUseCaseImpl(blockObservers, conditionValidators, actionRunners)
    }

    private fun generateUniqueId(): String = UUID.randomUUID().toString()


    private fun createCondition(
            id: String = generateUniqueId()
    ) = listOf(Condition(id))

    private fun createAction(
            id: String = generateUniqueId()
    ) = listOf(Action(id))

    private fun createDependency(
            id: String = generateUniqueId(),
            start: Block,
            end: Block,
            conditions: List<Condition> = createCondition(),
            actions: List<Action> = createAction()
    ) = Dependency(id, start, end, conditions, actions)

    private fun createScript(
            id: Long = generateUniqueId().hashCode().toLong(),
            name: String = generateUniqueId(),
            description: String = generateUniqueId(),
            enabled: Boolean = true,
            blocks: List<Block>,
            dependencies: List<Dependency>
    ) = Script(id, name, description, enabled, blocks, dependencies)

    private fun createBlock(
            id: String = generateUniqueId(),
            position: Position = Position(1, 1)
    ) = Block(id, position)


    @Test
    fun `when one dependency should listen to cahnges of top block`() {
        val a = createBlock()
        val b = createBlock()
        val dependency = createDependency(start = a, end = b)
        val script = createScript(
                blocks = listOf(a, b),
                dependencies = listOf(dependency)
        )

        whenever(blockObserver.execute(a.id)).then { Observable.empty<Optional<Block>>() }

        protocol.execute(script)

        verify(blockObserver).execute(a.id)
    }

    @Test
    fun `when one dependency and top block is second should listen to changes of second block`() {
        val a = createBlock()
        val b = createBlock()
        val dependency = createDependency(start = b, end = a)
        val script = createScript(
                blocks = listOf(a, b),
                dependencies = listOf(dependency)
        )

        whenever(blockObserver.execute(b.id)).then { Observable.empty<Optional<Block>>() }

        protocol.execute(script)

        verify(blockObserver).execute(b.id)
    }

    @Test
    fun `on changes of block should run validator`() {
        val a = createBlock()
        val b = createBlock()
        val dependency = createDependency(start = a, end = b)
        val script = createScript(
                blocks = listOf(a, b),
                dependencies = listOf(dependency)
        )

        val firstState = Block(a.id, a.position)
        val secondState = Block(a.id, a.position)

        whenever(blockObserver.execute(a.id)).then { Observable.just(
                firstState.toOptional(),
                secondState.toOptional()
        ) }

        protocol.execute(script)

        verify(validator).validate(dependency.conditions.first(), firstState.toOptional())
        verify(validator).validate(dependency.conditions.first(), secondState.toOptional())
    }

    @Test
    fun `when validated dependency should run action`() {
        val a = createBlock()
        val b = createBlock()
        val dependency = createDependency(start = a, end = b)
        val script = createScript(
                blocks = listOf(a, b),
                dependencies = listOf(dependency)
        )

        val firstState = Block(a.id, a.position)
        val secondState = Block(a.id, a.position)


        whenever(blockObserver.execute(a.id)).then { Observable.just(
                firstState.toOptional(),
                secondState.toOptional()
        ) }
        whenever(validator.validate(dependency.conditions.first(), secondState.toOptional())).then { true }

        protocol.execute(script)

        verify(actionRunner).runAction(dependency.actions.first(), b.id)
        verifyNoMoreInteractions(actionRunner)
    }
}