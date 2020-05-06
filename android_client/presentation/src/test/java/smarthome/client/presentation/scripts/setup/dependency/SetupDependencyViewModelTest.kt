package smarthome.client.presentation.scripts.setup.dependency

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import smarthome.client.domain.api.scripts.usecases.dependency.GetSetupDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.ObserveSetupDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.StartSetupDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.setup.CreateEmptyActionForDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.setup.CreateEmptyConditionsForDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.setup.GetDependencyUseCase
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver
import smarthome.client.presentation.scripts.setup.dependency.mock.MockAction
import smarthome.client.presentation.scripts.setup.dependency.mock.MockCondition
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
    
    private val dependencyId = "dependencyId"
    private val startBlock = "blockId1"
    private val endBlock = "blockId2"
    private val conditionId = "conditionId"
    private val condition = MockCondition(conditionId)
    private val actionId = "actionId"
    private val action = MockAction(actionId)
    private val condition_A = MockCondition("conditionA")
    private val condition_B = MockCondition("conditionB")
    private val dependency = Dependency(dependencyId, startBlock, endBlock, listOf(condition), listOf(action))
    
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    
    @Before
    fun setUp() {
        trampoline()
        createEmptyConditions = mock {
            on { execute(any()) }.then { listOf(condition_A, condition_B) }
        }
        createEmptyAction = mock {
            on { execute(any()) }.then { listOf(action) }
        }
        startSetupDependencyUseCase = mock {
            on { execute(any()) }.then {
                observedSetupDependency.onNext(dependency)
                dependency
            }
        }
        getDependencyUseCase = mock {
            on { execute(any()) }.then { dependency }
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
        verify(startSetupDependencyUseCase).execute(dependencyId)
    }
    
    @Test
    fun `when emit observed setup dependency should update conditions`() {
        val domainConditionId = "id"
        val domainCondition = MockCondition(domainConditionId)
        val currentDependency = setupContainerWithOneEmptyAndOneDomainConditions(domainCondition)
        
        val newData = "asdf"
        val newCondition = MockCondition(domainConditionId, newData)
        val newConditions = currentDependency.conditions.findAndModify(
            predicate = { it.id == domainConditionId },
            modify = { newCondition }
        )
        val newDetails = currentDependency.copy(conditions = newConditions)
        observedSetupDependency.onNext(newDetails)
        
        assertTrue {
            val containers = viewModel.conditionContainers.value!!
            val shouldHaveChanged = containers.first().allData[1] as MockCondition
            shouldHaveChanged.id == domainConditionId &&
                shouldHaveChanged.data == newData
        }
    }
    
    private fun setupContainerWithOneEmptyAndOneDomainConditions(domainCondition: Condition): Dependency {
        val dependency = dependency.copy(conditions = listOf(domainCondition))
        whenever(createEmptyConditions.execute(any()))
            .then { listOf(condition_A, condition_B) }
        
        whenever(startSetupDependencyUseCase.execute(any())).then {
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