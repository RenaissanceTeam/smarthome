package smarthome.client.presentation.scripts.setup.dependency

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import smarthome.client.domain.api.scripts.usecases.setup.CreateEmptyActionForDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.setup.CreateEmptyConditionsForDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.setup.GetDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.GetSetupDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.ObserveSetupDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.StartSetupDependencyUseCase
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.entity.script.dependency.condition.SimpleDependencyUnitId
import smarthome.client.presentation.scripts.setup.dependency.mock.MockActionData
import smarthome.client.presentation.scripts.setup.dependency.mock.MockConditionData_A
import smarthome.client.presentation.scripts.setup.dependency.mock.MockConditionData_B
import smarthome.client.presentation.scripts.setup.graph.MockBlockId
import smarthome.client.presentation.scripts.setup.graph.MockDependencyId
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver
import smarthome.client.util.findAndModify
import smarthome.client.util.trampoline
import kotlin.test.assertTrue

class SetupDependencyViewModelTest {
    private lateinit var viewModel: SetupDependencyViewModel
    private lateinit var createEmptyConditions: CreateEmptyConditionsForDependencyUseCase
    private lateinit var createEmptyAction: CreateEmptyActionForDependencyUseCase
    private lateinit var conditionModelsResolver: ConditionModelResolver
    private lateinit var getSetupDependencyUseCase: GetSetupDependencyUseCase
    private lateinit var getDependencyUseCase: GetDependencyUseCase
    private lateinit var startSetupDependencyUseCase: StartSetupDependencyUseCase
    private lateinit var observeSetupDependencyUseCase: ObserveSetupDependencyUseCase
    private lateinit var observedSetupDependency: PublishSubject<Dependency>
    
    private val dependencyId = MockDependencyId()
    private val startBlock = MockBlockId()
    private val endBlock = MockBlockId()
    private val conditionData = MockConditionData_A()
    private val conditionId = SimpleDependencyUnitId()
    private val condition = Condition(conditionId, conditionData)
    private val actionData = MockActionData()
    private val actionId = SimpleDependencyUnitId()
    private val action = Action(actionId, actionData)
    private val conditionData_A = MockConditionData_A()
    private val conditionData_B = MockConditionData_B()
    private val condition_A = Condition(SimpleDependencyUnitId(), conditionData_A)
    private val condition_B = Condition(SimpleDependencyUnitId(), conditionData_B)
    private val dependency = Dependency(dependencyId, startBlock, endBlock, listOf(condition), listOf(action))
    
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    
    @Before
    fun setUp() {
        trampoline()
        createEmptyConditions = mock {
            on { execute(any(), any()) }.then { listOf(condition_A, condition_B) }
        }
        createEmptyAction = mock {
            on { execute(any(), any()) }.then { listOf(action) }
        }
        startSetupDependencyUseCase = mock {
            on { execute(any(), any()) }.then {
                observedSetupDependency.onNext(dependency)
                dependency
            }
        }
        getDependencyUseCase = mock {
            on { execute(any(), any()) }.then { dependency }
        }
        conditionModelsResolver = mock {}
        observedSetupDependency = PublishSubject.create()
        observeSetupDependencyUseCase = mock {
            on { execute() }.then { observedSetupDependency }
        }
        getSetupDependencyUseCase = mock {
            on { execute() }.then { dependency }
        }
        
        startKoin {
            modules(module {
                single { createEmptyConditions }
                single { createEmptyAction }
                single { conditionModelsResolver }
                single { getSetupDependencyUseCase }
                single { getDependencyUseCase }
                single { startSetupDependencyUseCase }
                single { observeSetupDependencyUseCase }
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
            viewModel.conditionContainers.value != null
        }
    }
    
    @Test
    fun `when set dependency should start setup`() {
        viewModel.setDependencyId(dependencyId)
        verify(startSetupDependencyUseCase).execute(any(), eq(dependencyId))
    }
    
    @Test
    fun `when emit observed setup dependency should update conditions`() {
        val domainData = MockConditionData_B()
        val domainConditionId = SimpleDependencyUnitId()
        val domainCondition = Condition(domainConditionId, domainData)
        val currentDependency = setupContainerWithOneEmptyAndOneDomainConditions(domainCondition)
        
        val newData = MockConditionData_B()
        val newCondition = Condition(domainConditionId, newData)
        val newConditions = currentDependency.conditions.findAndModify(
            predicate = { it.id == domainConditionId },
            modify = { newCondition }
        )
        val newDetails = currentDependency.copy(conditions = newConditions)
        observedSetupDependency.onNext(newDetails)
        
        assertTrue {
            val containers = viewModel.conditionContainers.value!!
            val shouldHaveChanged = containers.first().allData[1]
            shouldHaveChanged.id == domainConditionId &&
                shouldHaveChanged.data == newData
        }
    }
    
    private fun setupContainerWithOneEmptyAndOneDomainConditions(domainCondition: Condition): Dependency {
        val dependency = dependency.copy(conditions = listOf(domainCondition))
        whenever(createEmptyConditions.execute(any(), any()))
            .then { listOf(condition_A, condition_B) }
        
        whenever(startSetupDependencyUseCase.execute(any(), any())).then {
            observedSetupDependency.onNext(dependency)
            dependency
        }
        viewModel.setDependencyId(dependencyId)
        assertTrue {
            val containers = viewModel.conditionContainers.value!!
            containers.size == 1
                && containers.first().allData.size == 2
                && containers.first().allData[1] == domainCondition
        }
        
        return dependency
    }
}