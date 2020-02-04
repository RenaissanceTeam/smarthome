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
import smarthome.client.entity.Controller
import smarthome.client.entity.script.BlockId
import smarthome.client.entity.script.Position
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.DependencyState
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlockResolver
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
    private lateinit var blockResolver: GraphBlockResolver
    private val blockId = MockBlockId()
    private val otherBlockId = MockBlockId()
    private val dependencyId = "someId"
    
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        observeControllerUseCase =
            mock { on { execute(any()) }.then { Observable.empty<DataStatus<Controller>>() } }
        events = PublishSubject.create()
        eventBus = mock { on { observe() }.then { events } }
        blockResolver = mock { }
        startKoin {
            modules(module {
                single { observeControllerUseCase }
                single { eventBus }
                single { blockResolver }
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
    
    private fun setupResolveIdentifier(event: GraphDragEvent, id: BlockId = blockId) {
        whenever(blockResolver.resolveIdentifier(event)).then { id }
    }
    
    private fun setupMockingBlock(id: MockBlockId = blockId,
                                            position: Position = position1_1): MockBlockState {
        return MockBlockState(id, position)
    }
    
    private fun setupResolveBlock(event: GraphDragEvent, blockState: BlockState) {
        whenever(blockResolver.createBlock(event)).then { blockState }
    }
    
    private fun assertHasBlockValue(id: BlockId = blockId): BlockState {
        val blocks = viewModel.blocks.value
        assertNotNull(blocks)
    
        val addedBlock = blocks.find { it.block.id == id }
        assertNotNull(addedBlock)
        
        return addedBlock
    }
    
    private fun assertHasDependency(id: String = dependencyId): DependencyState {
        val dependencies = viewModel.dependencies.value
        assertNotNull(dependencies)
        
        val dependency = dependencies[id]
        assertNotNull(dependency)
        
        return dependency
    }
    
    @Test
    fun `when dependency tip on some block should emit`() {
        addBlock()
        viewModel.dependencyTipOnBlock(from = otherBlockId, to = blockId)
        
        val block = assertHasBlockValue(blockId)
        assertTrue(block.border.isVisible)
    }
    
    private fun addBlock() {
        events.onNext(createDragEvent(status = DRAG_DROP, to = GRAPH, from = CONTROLLERS_HUB))
    }
    
    @Test
    fun `when save should serialize block types, ids and positions`() {
    
    }
    
    @Test
    fun `when end creating dependency on graph then should cancel and remove dependency`() {
        addBlock()
        events.onNext(DependencyEvent(
            id = dependencyId,
            startId = blockId,
            status = DEPENDENCY_MOVE,
            rawEndPosition = Position(22, 22)
        ))
    
        viewModel.cancelCreatingDependency(dependencyId)
        val dependencies = viewModel.dependencies.value
        assertNotNull(dependencies)
    
        val dependency = dependencies[dependencyId]
        assertNull(dependency)
    }
    
    @Test
    fun `when end creating dependency on other block should start setup of dependency`() {
        addBlock()
        emitStartDependencyFromOther()
        emitMoveDependencyFromOther()
    
        viewModel.addDependency(dependencyId, otherBlockId, blockId)
        val dependency = assertHasDependency()
        assertEquals(otherBlockId, dependency.startBlock)
        assertEquals(blockId, dependency.endBlock)
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