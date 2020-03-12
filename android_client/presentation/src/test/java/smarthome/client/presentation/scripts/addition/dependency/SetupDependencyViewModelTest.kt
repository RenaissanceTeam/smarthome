package smarthome.client.presentation.scripts.addition.dependency

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
import smarthome.client.domain.api.scripts.usecases.CreateEmptyActionForBlockUseCase
import smarthome.client.domain.api.scripts.usecases.CreateEmptyConditionsForBlockUseCase
import smarthome.client.domain.api.scripts.usecases.GetDependencyDetailsUseCase
import smarthome.client.domain.api.scripts.usecases.UpdateDependencyDetailsUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.GetSetupDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.ObserveSetupDependencyUseCase
import smarthome.client.domain.api.scripts.usecases.dependency.StartSetupDependencyUseCase
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyDetails
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.entity.script.dependency.condition.SimpleDependencyUnitId
import smarthome.client.presentation.scripts.addition.dependency.mock.MockActionData
import smarthome.client.presentation.scripts.addition.dependency.mock.MockConditionData_A
import smarthome.client.presentation.scripts.addition.dependency.mock.MockConditionData_B
import smarthome.client.presentation.scripts.addition.graph.MockBlockId
import smarthome.client.presentation.scripts.addition.graph.MockDependencyId
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver
import smarthome.client.util.findAndModify
import smarthome.client.util.trampoline
import smarthome.client.util.withRemoved
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
    private lateinit var observeSetupDependencyUseCase: ObserveSetupDependencyUseCase
    private lateinit var observedSetupDependency: PublishSubject<DependencyDetails>
    
    private val dependencyId = MockDependencyId()
    private val startBlock = MockBlockId()
    private val endBlock = MockBlockId()
    private val dependency = Dependency(dependencyId, startBlock, endBlock)
    private val conditionData = MockConditionData_A()
    private val conditionId = SimpleDependencyUnitId()
    private val condition = Condition(conditionId, conditionData)
    private val actionData = MockActionData()
    private val actionId = SimpleDependencyUnitId()
    private val action = Action(actionId, actionData)
    private val dependencyDetails = DependencyDetails(dependency, listOf(condition), listOf(action))
    private val conditionData_A = MockConditionData_A()
    private val conditionData_B = MockConditionData_B()
    private val condition_A = Condition(SimpleDependencyUnitId(), conditionData_A)
    private val condition_B = Condition(SimpleDependencyUnitId(), conditionData_B)
    
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
                observedSetupDependency.onNext(dependencyDetails)
                dependencyDetails
            }
        }
        getDependencyDetailsUseCase = mock {
            on { execute(any(), any()) }.then { dependencyDetails }
        }
        updateDependencyDetailsUseCase = mock {}
        conditionModelsResolver = mock {}
        observedSetupDependency = PublishSubject.create()
        observeSetupDependencyUseCase = mock {
            on { execute() }.then { observedSetupDependency }
        }
        getSetupDependencyUseCase = mock {
            on { execute() }.then { dependencyDetails }
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
    fun `when creating dependency condition containers their size should equal condition details`() {
        viewModel.setDependencyId(dependencyId)
        
        assertTrue {
            viewModel.conditionContainers.value!!.size == dependencyDetails.conditions.size
        }
    }
    
    @Test
    fun `size of container units should equal size of empty conditions`() {
        whenever(createEmptyConditions.execute(any(), any()))
            .then { listOf(condition_A, condition_B) }
        
        viewModel.setDependencyId(dependencyId)
    
        val firstContainer = viewModel.conditionContainers.value!!.first()
        assertEquals(2, firstContainer.allData.units.size)
    }
    
    @Test
    fun `each container state should have empty units with one replaced item from details`() {
        val domainData = MockConditionData_B()
        val domainCondition = Condition(SimpleDependencyUnitId(), domainData)
        whenever(createEmptyConditions.execute(any(), any()))
            .then { listOf(condition_A, condition_B) }
    
        whenever(startSetupDependencyUseCase.execute(any(), any())).then {
            val details = dependencyDetails.copy(conditions = listOf(domainCondition))
            observedSetupDependency.onNext(details)
            details
        }
        
        viewModel.setDependencyId(dependencyId)
        
        assertTrue {
            val firstContainer = viewModel.conditionContainers.value!!.first()
            firstContainer.allData.units[0].data == conditionData_A
                && firstContainer.allData.units[1].data == domainData
        }
    }
    
    @Test
    fun `each container should have valid selected item which is domain unit in empty units list`() {
        whenever(createEmptyConditions.execute(any(), any()))
            .then { listOf(condition_A, condition_B) }
    
        whenever(startSetupDependencyUseCase.execute(any(), any())).then {
            val details = dependencyDetails.copy(conditions = listOf(condition_B))
            observedSetupDependency.onNext(details)
            details
        }
        
        viewModel.setDependencyId(dependencyId)
        assertTrue {
            val firstContainer = viewModel.conditionContainers.value!!.first()
            val selectedInFirstContainer = firstContainer.selectedUnitIndex
            
            selectedInFirstContainer == 1
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
            val shouldHaveChanged = containers.first().allData.units[1]
            shouldHaveChanged.id == domainConditionId &&
                shouldHaveChanged.data == newData
        }
    }
    
    private fun setupContainerWithOneEmptyAndOneDomainConditions(domainCondition: Condition): DependencyDetails {
        val dependency = dependencyDetails.copy(conditions = listOf(domainCondition))
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
                && containers.first().allData.units.size == 2
                && containers.first().allData.units[1] == domainCondition
        }
        
        return dependency
    }
    
    @Test
    fun `when emit setup dependency with new added condition should add container with empty conditions`() {
        val currentDependency = setupContainerWithOneEmptyAndOneDomainConditions(condition_B)
        
        val newData = MockConditionData_B()
        val newConditionId = SimpleDependencyUnitId()
        val newCondition = Condition(newConditionId, newData)
        
        addContainerForCondition(currentDependency, newCondition)
        
        assertTrue {
            val containers = viewModel.conditionContainers.value!!
            val newContainer = containers[1]
            newContainer.allData.units.containsThat {
                it.id == newConditionId && it.data == newData
            }
        }
    }
    
    private fun addContainerForCondition(currentDependency: DependencyDetails, condition: Condition) {
        val newConditions = currentDependency.conditions + condition
        val newDetails = currentDependency.copy(conditions = newConditions)
        observedSetupDependency.onNext(newDetails)
    }
    
    
    @Test
    fun `when emit setup dependency without previously added unit should remove container`() {
        val currentDependency = setupContainerWithOneEmptyAndOneDomainConditions(condition_B)
        
        val newData = MockConditionData_B()
        val newConditionId = SimpleDependencyUnitId()
        val newCondition = Condition(newConditionId, newData)
        
        addContainerForCondition(currentDependency, newCondition)
        assertTrue {
            viewModel.conditionContainers.value!!.size == 2
        }
        val newConditions = currentDependency.conditions.withRemoved { it == newCondition }
        val newDetails = currentDependency.copy(conditions = newConditions)
        
        observedSetupDependency.onNext(newDetails)
        
        assertTrue {
            val containers = viewModel.conditionContainers.value!!
            containers.size == 1
        }
    }
}