package smarthome.raspberry.scripts.domain

import com.nhaarman.mockitokotlin2.mock
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.entity.script.Script
import smarthome.raspberry.scripts.api.domain.ConditionValidator
import smarthome.raspberry.scripts.api.domain.ObserveBlockStatesUseCase
import smarthome.raspberry.scripts.api.domain.RunScriptActionUseCase

class ScriptProtocolImplTest {

    private lateinit var protocol: ScriptProtocolImpl
    private lateinit var observeBlockStatesUseCase: ObserveBlockStatesUseCase
    private lateinit var conditionValidators: Map<String, ConditionValidator>
    private lateinit var runScriptActionUseCase: RunScriptActionUseCase
    private lateinit var script: Script

    @Before
    fun setUp() {

        observeBlockStatesUseCase = mock { }
        runScriptActionUseCase = mock { }
        conditionValidators = mapOf()
        script = Script(
                name = "",
                description = "",
                enabled = true,
                blocks = listOf(),
                dependencies = listOf()
        )
        protocol = ScriptProtocolImpl(observeBlockStatesUseCase, conditionValidators, runScriptActionUseCase)
    }

    @Test
    fun `compile`() {

    }
}