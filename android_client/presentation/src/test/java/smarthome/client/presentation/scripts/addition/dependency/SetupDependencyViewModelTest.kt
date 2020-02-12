package smarthome.client.presentation.scripts.addition.dependency

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import smarthome.client.domain.api.scripts.usecases.CreateEmptyActionForBlockUseCase
import smarthome.client.domain.api.scripts.usecases.CreateEmptyConditionsForBlockUseCase
import smarthome.client.domain.api.scripts.usecases.GetDependencyDetailsUseCase
import smarthome.client.domain.api.scripts.usecases.UpdateDependencyDetailsUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.GetSetupDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.StartSetupDependencyUseCase
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyDetails
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.entity.script.dependency.condition.SimpleDependencyUnitId
import smarthome.client.presentation.scripts.addition.dependency.mock.MockAction
import smarthome.client.presentation.scripts.addition.dependency.mock.MockConditionData_A
import smarthome.client.presentation.scripts.addition.dependency.mock.MockConditionData_B
import smarthome.client.presentation.scripts.addition.graph.MockBlockId
import smarthome.client.presentation.scripts.addition.graph.MockDependencyId
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SetupDependencyViewModelTest {
    
    private lateinit var viewModel: SetupDependencyViewModel
    private lateinit var createEmptyConditions: CreateEmptyConditionsForBlockUseCase
    private lateinit var createEmptyAction: CreateEmptyActionForBlockUseCase
    private lateinit var updateDependencyDetailsUseCase: UpdateDependencyDetailsUseCase
    private lateinit var conditionModelsResolver: ConditionModelResolver
    private lateinit var getSetupDependencyUseCase: GetSetupDependencyUseCase
    private lateinit var getDependencyDetailsUseCase: GetDependencyDetailsUseCase
    private lateinit var startSetupDependencyUseCase: StartSetupDependencyUseCase
    
    private val dependencyId = MockDependencyId()
    private val startBlock = MockBlockId()
    private val endBlock = MockBlockId()
    private val dependency = Dependency(dependencyId, startBlock, endBlock)
    private val conditionData = MockConditionData_A()
    private val conditionId = SimpleDependencyUnitId()
    private val condition = Condition(conditionId, conditionData)
    private val action = MockAction(dependencyId)
    private val dependencyDetails = DependencyDetails(dependency, listOf(condition), listOf(action))
    private val conditionData_A = MockConditionData_A()
    private val conditionData_B = MockConditionData_B()
    private val condition_A = Condition(SimpleDependencyUnitId(), conditionData_A)
    private val condition_B = Condition(SimpleDependencyUnitId(), conditionData_B)
    
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    
    @Before
    fun setUp() {
        createEmptyConditions = mock {
            on { execute(any(), any()) }.then { listOf(condition_A, condition_B) }
        }
        createEmptyAction = mock {}
        startSetupDependencyUseCase = mock {}
        getDependencyDetailsUseCase = mock {
            on { execute(any(), any()) }.then { dependencyDetails }
        }
        updateDependencyDetailsUseCase = mock {}
        conditionModelsResolver = mock {}
        getSetupDependencyUseCase = mock {
            on { execute(any(), any()) }.then { dependencyDetails }
        }
        
        startKoin {
            modules(module {
                single { createEmptyConditions }
                single { createEmptyAction }
                single { updateDependencyDetailsUseCase }
                single { conditionModelsResolver }
                single { getSetupDependencyUseCase }
                single { getDependencyDetailsUseCase }
                single { startSetupDependencyUseCase }
            })
        }
        
        viewModel = SetupDependencyViewModel()
    }
    
    @After
    fun tearDown() {
        stopKoin()
    }
    
    @Test
    fun `when set dependency id should get dependency details and emit conditions states`() {
        viewModel.setDependencyId(dependencyId)
        
        assertTrue {
            viewModel.conditions.value != null
        }
    }
    
    @Test
    fun `when creating dependency condition containers their size should equal condition details`() {
        viewModel.setDependencyId(dependencyId)
        
        assertTrue {
            viewModel.conditions.value!!.size == dependencyDetails.conditions.size
        }
    }
    
    @Test
    fun `size of container units should equal size of empty conditions`() {
        whenever(createEmptyConditions.execute(any(), any()))
            .then { listOf(condition_A, condition_B) }
    
        whenever(getDependencyDetailsUseCase.execute(any(), any())).then {
            dependencyDetails.copy(conditions = listOf(condition_B))
        }
        
        viewModel.setDependencyId(dependencyId)
        
        val firstContainer = viewModel.conditions.value!!.first()
        assertEquals(2, firstContainer.conditions.size)
    }
    
    @Test
    fun `each container state should have empty units with one replaced item from details`() {
        val domainData = MockConditionData_B()
        val domainCondition = Condition(SimpleDependencyUnitId(), domainData)
        whenever(createEmptyConditions.execute(any(), any()))
            .then { listOf(condition_A, condition_B) }
    
        whenever(getDependencyDetailsUseCase.execute(any(), any())).then {
            dependencyDetails.copy(conditions = listOf(domainCondition))
        }
        
        viewModel.setDependencyId(dependencyId)
        
        assertTrue {
            val firstContainer = viewModel.conditions.value!!.first()
            firstContainer.conditions[0].data == conditionData_A
                && firstContainer.conditions[1].data == domainData
        }
    }
    
    @Test
    fun `each container should have valid selected item which is domain unit in empty units list`() {
        whenever(createEmptyConditions.execute(any(), any()))
            .then { listOf(condition_A, condition_B) }
    
        whenever(getDependencyDetailsUseCase.execute(any(), any())).then {
            dependencyDetails.copy(conditions = listOf(condition_B))
        }
        
        viewModel.setDependencyId(dependencyId)
        assertTrue {
            val firstContainer = viewModel.conditions.value!!.first()
            val selectedInFirstContainer = firstContainer.selected
            
            selectedInFirstContainer == 1
        }
    }
}