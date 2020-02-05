package smarthome.client.presentation.scripts.addition.graph

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.domain.api.scripts.usecases.CheckIfDependencyPossibleUseCase
import smarthome.client.domain.api.scripts.usecases.ObserveBlocksUseCase
import smarthome.client.domain.api.scripts.usecases.ObserveDependenciesUseCase
import smarthome.client.entity.Controller
import smarthome.client.entity.script.Block
import smarthome.client.entity.script.BlockId
import smarthome.client.entity.script.Dependency
import smarthome.client.entity.script.Position
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.IDLE
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.MOVING
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.MovingDependency
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.addition.graph.events.GraphEvent
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_MOVE
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_START
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DependencyEvent
import smarthome.client.presentation.scripts.addition.graph.events.drag.*
import smarthome.client.util.DataStatus
import kotlin.test.*

class ScriptGraphViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    
    private lateinit var observeControllerUseCase: ObserveControllerUseCase
    private lateinit var eventBus: GraphEventBus
    private lateinit var events: PublishSubject<GraphEvent>
    private val position1_1 = Position(1, 1)
    private lateinit var viewModel: ScriptGraphViewModel
    private lateinit var observeBlocksUseCase: ObserveBlocksUseCase
    private lateinit var observeDependenciesUseCase: ObserveDependenciesUseCase
    private lateinit var dragBlockEventsHandler: DragBlockEventsHandler
    private lateinit var dependencyEventsHandler: DependencyEventsHandler
    private lateinit var checkIfDependencyPossibleUseCase: CheckIfDependencyPossibleUseCase
    private lateinit var blockToNewGraphBlockStateMapper: BlockToNewGraphBlockStateMapper
    private lateinit var blocksObservable: PublishSubject<List<Block>>
    private lateinit var dependenciesObservable: PublishSubject<List<Dependency>>
    private val blockId = MockBlockId()
    private val otherBlockId = MockBlockId()
    private val block = MockBlock(blockId, position1_1)
    private val otherBlock = MockBlock(otherBlockId, position1_1)
    private val blockState = MockBlockState(block)
    private val otherBlockState = MockBlockState(otherBlock)
    private val dependencyId = MockDependencyId()
    
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        blocksObservable = PublishSubject.create()
        dependenciesObservable = PublishSubject.create()
        observeControllerUseCase =
            mock { on { execute(any()) }.then { Observable.empty<DataStatus<Controller>>() } }
        observeBlocksUseCase = mock {
            on { execute() }.then { blocksObservable }
        }
        observeDependenciesUseCase = mock {
            on { execute() }.then { dependenciesObservable }
        }
        events = PublishSubject.create()
        eventBus = mock { on { observe() }.then { events } }
        dragBlockEventsHandler = mock {}
        dependencyEventsHandler = mock {}
        checkIfDependencyPossibleUseCase = mock {
            on { execute(any(), any()) }.then { true }
        }
        blockToNewGraphBlockStateMapper = mock {
            on { map(any()) }.then { blockState }
        }
        startKoin {
            modules(module {
                single { observeControllerUseCase }
                single { eventBus }
                single { observeBlocksUseCase }
                single { dragBlockEventsHandler }
                single { dependencyEventsHandler }
                single { blockToNewGraphBlockStateMapper }
                single { checkIfDependencyPossibleUseCase }
                single { observeDependenciesUseCase }
            })
        }
        
        viewModel = ScriptGraphViewModel()
    }
    
    
    @After
    fun tearDown() {
        stopKoin()
    }
    
    @Test
    fun `when call to drop should emit drop event for event bus`() {
        val event = createDragEvent(status = DRAG_START, from = CONTROLLERS_HUB)
        viewModel.onDropped(event, position1_1)
        
        verify(eventBus).addEvent(argThat {
            this is MockDragEvent
                && this.dragInfo.id == blockId
                && this.dragInfo.to == GRAPH
                && this.dragInfo.status == DRAG_DROP
        })
    }
    
    @Test
    fun `when drop block should calculate position as drop position minus touch position`() {
        val event = createDragEvent(status = DRAG_START, from = CONTROLLERS_HUB)
        
        viewModel.onDropped(event, position1_1)
        verify(eventBus).addEvent(argThat {
            this is MockDragEvent
                && this.dragInfo.position == Position(0, 0)
        })
    }
    
    private fun createDragEvent(
        id: BlockId = blockId,
        status: String,
        from: String = UNKNOWN,
        to: String = UNKNOWN,
        touchPosition: Position = position1_1,
        position: Position = position1_1): MockDragEvent {
        return MockDragEvent(CommonDragInfo(id, status, from, to, touchPosition, position))
    }
    
    private fun assertHasBlockValue(id: BlockId = blockId): BlockState {
        val blocks = viewModel.blocks.value
        assertNotNull(blocks)
    
        val addedBlock = blocks.find { it.block.id == id }
        assertNotNull(addedBlock)
        
        return addedBlock
    }
    
    @Test
    fun `when dependency tip on some block should emit with visible border`() {
        addBlock()
        viewModel.dependencyTipOnBlock(from = otherBlockId, to = blockId)
        
        val block = assertHasBlockValue(blockId)
        assertTrue(block.border.isVisible)
    }
    
    private fun addBlock() {
        blocksObservable.onNext(listOf(block))
    }
    
    private fun addTwoBlocks() {
        blocksObservable.onNext(listOf(block, otherBlock))
    }
    
    @Test
    fun `when save should serialize block types, ids and positions`() {
    
    }
    
    @Test
    fun `when end creating dependency on graph then should emit idle moving dependency`() {
        viewModel.movingDependency.value = MovingDependency(
            MockDependencyId(),
            otherBlockId,
            MOVING,
            position1_1
        )
        
        viewModel.cancelCreatingDependency()
        
        val dependency = viewModel.movingDependency.value
        assertNotNull(dependency)
        assertTrue { dependency.status == IDLE }
    }
    
    @Test
    fun `when end creating dependency on other block should start setup of dependency`() {
        viewModel.addDependency(dependencyId, otherBlockId, blockId)
        
        // todo check that started setup flow with two ids
    }
    
    private fun emitMoveDependencyFromOther() {
        events.onNext(DependencyEvent(
            id = dependencyId,
            startId = otherBlockId,
            status = DEPENDENCY_MOVE,
            rawEndPosition = Position(22, 22)
        ))
    }
    
    private fun emitStartDependencyFromOther() {
        events.onNext(DependencyEvent(
            id = dependencyId,
            startId = otherBlockId,
            status = DEPENDENCY_START,
            rawEndPosition = position1_1
        ))
    }
    
    @Test
    fun `when add dependency should hide border on the added block`() {
        addBlock()
        emitStartDependencyFromOther()
        emitMoveDependencyFromOther()
        
        viewModel.dependencyTipOnBlock(otherBlockId, blockId)
        assertTrue(assertHasBlockValue(blockId).border.isVisible)
        
        viewModel.addDependency(dependencyId, otherBlockId, blockId)
        assertFalse(assertHasBlockValue(blockId).border.isVisible)
    }
    
    @Test
    fun `when remove block with dependency should remove associated dependencies`() {
    
    }
}