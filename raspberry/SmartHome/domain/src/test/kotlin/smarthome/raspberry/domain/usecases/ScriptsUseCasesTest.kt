package smarthome.raspberry.domain.usecases

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.library.common.scripts.Action
import smarthome.library.common.scripts.Condition
import smarthome.library.common.scripts.Script
import smarthome.raspberry.domain.ScriptsRepository


class ScriptsUseCasesTest {

    private val scriptsRepo = mock<ScriptsRepository>()
    private val scriptsUseCases = ScriptsUseCases(scriptsRepo)
    private val passingCondition = mock<Condition>()
    private val action = mock<Action>()

    @Before
    fun setUp() {
        runBlocking {
            whenever(passingCondition.satisfy()).then { true }
        }
    }

    @Test
    fun onScriptAdded_shouldSaveToRepo() {
        val newScript = Script()

        runBlocking {
            scriptsUseCases.onNewScript(newScript)
            verify(scriptsRepo).save(newScript)
        }
    }

    @Test
    fun onScriptAdded_havePassingCondition_shouldFireAction() {
        val newScript = Script(conditions = mutableListOf(passingCondition),
                actions = mutableListOf(action))
        runBlocking {
            scriptsUseCases.onNewScript(newScript)
            verify(action).run()
        }
    }

    @Test
    fun onScriptAdded_noPassingConditions_shouldScheduleEvent() {
        val condition = mock<Condition>()

        val newScript = Script(conditions = mutableListOf(condition),
                actions = mutableListOf(action))

        runBlocking {
            whenever(condition.satisfy()).then { false }
            scriptsUseCases.onNewScript(newScript)

            verify(action, never()).run()

            whenever(condition.satisfy()).then { true }
            whenever(scriptsRepo.scripts).then { listOf(newScript) }
            scriptsUseCases.conditionsChanged()

            verify(action).run()
        }
    }
}