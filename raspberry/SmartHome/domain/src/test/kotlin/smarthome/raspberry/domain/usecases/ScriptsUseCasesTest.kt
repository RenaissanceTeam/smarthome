package smarthome.raspberry.domain.usecases

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.*
import org.junit.Test
import smarthome.library.common.scripts.Script
import smarthome.raspberry.domain.ScriptsRepository


class ScriptsUseCasesTest {

    private val scriptsRepo = mock<ScriptsRepository>()
    private val scriptsUseCases = ScriptsUseCases(scriptsRepo)

    @Test
    fun onScriptAdded_shouldSaveToRepo() {
        val newScript = Script()

        scriptsUseCases.onNewScript(newScript)
        verify(scriptsRepo).save(newScript)
    }
}