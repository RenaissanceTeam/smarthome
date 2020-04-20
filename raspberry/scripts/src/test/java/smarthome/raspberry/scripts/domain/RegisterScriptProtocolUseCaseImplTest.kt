package smarthome.raspberry.scripts.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.entity.script.*
import smarthome.raspberry.scripts.api.domain.ConditionValidator
import smarthome.raspberry.scripts.api.domain.ObserveBlockStatesUseCase
import smarthome.raspberry.scripts.api.domain.RunScriptActionUseCase
import java.util.*

class RegisterScriptProtocolUseCaseImplTest {

    private lateinit var protocol: RegisterScriptProtocolUseCaseImpl
    private lateinit var observeBlockStatesUseCase: ObserveBlockStatesUseCase
    private lateinit var conditionValidators: Map<String, ConditionValidator>
    private lateinit var runScriptActionUseCase: RunScriptActionUseCase
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

        observeBlockStatesUseCase = mock { }
        runScriptActionUseCase = mock { }

        validator = mock { }
        conditionValidators = mapOf("ConditionValidator" to validator)
        script = Script(
                name = "",
                description = "",
                enabled = true,
                blocks = listOf(),
                dependencies = listOf()
        )
        protocol = RegisterScriptProtocolUseCaseImpl(observeBlockStatesUseCase, conditionValidators, runScriptActionUseCase)
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

        whenever(observeBlockStatesUseCase.execute(a.id)).then { Observable.empty<Block>() }

        protocol.execute(script)

        verify(observeBlockStatesUseCase).execute(a.id)
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

        whenever(observeBlockStatesUseCase.execute(b.id)).then { Observable.empty<Block>() }

        protocol.execute(script)

        verify(observeBlockStatesUseCase).execute(b.id)
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

        whenever(observeBlockStatesUseCase.execute(a.id)).then { Observable.just(firstState, secondState) }

        protocol.execute(script)

        verify(validator).validate(dependency.conditions.first(), firstState)
        verify(validator).validate(dependency.conditions.first(), secondState)
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

        whenever(observeBlockStatesUseCase.execute(a.id)).then { Observable.just(firstState, secondState) }
        whenever(validator.validate(dependency.conditions.first(), secondState)).then { true }

        protocol.execute(script)

        verify(runScriptActionUseCase).execute(b, dependency.actions.first())
        verifyNoMoreInteractions(runScriptActionUseCase)
    }
}