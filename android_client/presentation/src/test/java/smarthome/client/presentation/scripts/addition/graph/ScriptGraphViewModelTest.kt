package smarthome.client.presentation.scripts.addition.graph

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
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
import smarthome.client.presentation.scripts.addition.graph.blockviews.controller.ControllerBlock
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.DependencyState
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlock
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlockResolver
import smarthome.client.presentation.scripts.addition.graph.events.GraphEvent
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_MOVE
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_START
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DependencyEvent
import smarthome.client.presentation.scripts.addition.graph.events.drag.*
import smarthome.client.presentation.scripts.addition.graph.identifier.ControllerGraphBlockIdentifier
import smarthome.client.presentation.scripts.addition.graph.identifier.GraphBlockIdentifier
import smarthome.client.presentation.util.Position
import smarthome.client.util.DataStatus
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ScriptGraphViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    
    private lateinit var observeControllerUseCase: ObserveControllerUseCase
    private lateinit var eventBus: GraphEventBus
    private lateinit var events: PublishSubject<GraphEvent>
    private val position1_1 = Position(1f, 1f)
    private lateinit var viewModel: ScriptGraphViewModel
    private lateinit var blockResolver: GraphBlockResolver
    private val controllerId = 123332L
    private val blockId = ControllerGraphBlockIdentifier(controllerId)
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
        val event = createControllerDragEvent(status = DRAG_START, from = CONTROLLERS_HUB)
        viewModel.onDropped(event, position1_1)
        
        verify(eventBus).addEvent(argThat {
            this is ControllerDragEvent
                && this.id == controllerId
                && this.dragInfo.to == GRAPH
                && this.dragInfo.status == DRAG_DROP
        })
    }
    
    @Test
    fun `when drop block should calculate position as drop position minus touch position`() {
    
        val event = createControllerDragEvent(status = DRAG_START, from = CONTROLLERS_HUB)
        
        viewModel.onDropped(event, position1_1)
        verify(eventBus).addEvent(argThat {
            this is ControllerDragEvent
                && this.dragInfo.position == Position(
                0f, 0f)
        })
    }
    
    @Test
    fun `when drop controller from hub should resolve block for it and emit`() {
        val dropEvent =
            createControllerDragEvent(status = DRAG_DROP, to = GRAPH, from = CONTROLLERS_HUB)
    
        setupResolveIdentifier(dropEvent)
        val block = setupMockingControllerBlock()
        setupResolveBlock(dropEvent, block)
        
        events.onNext(dropEvent)
        
        val addedBlock = assertHasBlockValue()
        
        assertTrue(addedBlock is ControllerBlock)
        assertThat(addedBlock.id).isEqualTo(blockId)
        assertThat(addedBlock.position).isEqualTo(position1_1)
    }
    
    private fun createControllerDragEvent(id: Long = controllerId, status: String,
                                          from: String = UNKNOWN,
                                          to: String = UNKNOWN,
                                          touchPosition: Position = position1_1,
                                          position: Position = position1_1): ControllerDragEvent {
        return ControllerDragEvent(id, CommonDragInfo(status, from, to, touchPosition, position))
    }
    
    private fun setupResolveIdentifier(event: GraphDragEvent, id: GraphBlockIdentifier = blockId) {
        whenever(blockResolver.resolveIdentifier(event)).then { id }
    }
    
    private fun setupMockingControllerBlock(id: ControllerGraphBlockIdentifier = blockId,
                                            position: Position = position1_1): ControllerBlock {
        return ControllerBlock(
            id, position)
    }
    
    private fun setupResolveBlock(event: GraphDragEvent, block: GraphBlock) {
        whenever(blockResolver.createBlock(event)).then { block }
    }
    
    private fun assertHasBlockValue(id: GraphBlockIdentifier = blockId): GraphBlock {
        val blocks = viewModel.blocks.value
        assertNotNull(blocks)
    
        val addedBlock = blocks[id]
        assertNotNull(addedBlock)
        
        return addedBlock
    }
    
    @Test
    fun `when start drag on graph should hide dragged block`() {
        val dragEvent = createControllerDragEvent(status = DRAG_START, from = GRAPH)
        val block = setupMockingControllerBlock()
        setupResolveIdentifier(dragEvent)
        setupResolveBlock(dragEvent, block)
        
        events.onNext(dragEvent)
        
        val draggedBlock = assertHasBlockValue()
        assertTrue(draggedBlock is ControllerBlock)
        assertTrue(!draggedBlock.visible)
    }
    
    @Test
    fun `when drag from graph and drop to graph should emit block with new position`() {
        val block = setupMockingControllerBlock()

        val dragEvent = createControllerDragEvent(status = DRAG_START, from = GRAPH)
        
        setupResolveIdentifier(dragEvent)
        setupResolveBlock(dragEvent, block)
        
        events.onNext(dragEvent)
        
        val draggedBlock = assertHasBlockValue()
        assertTrue(draggedBlock is ControllerBlock && !draggedBlock.visible)
    
        // drop
        val droppedAt = Position(12f, 22f)
        val dropEvent = createControllerDragEvent(status = DRAG_DROP, to = GRAPH, from = GRAPH, position = droppedAt)
    
        setupResolveIdentifier(dropEvent)
        setupResolveBlock(dropEvent, block)
    
        events.onNext(dropEvent)
    
        val droppedBlock = assertHasBlockValue()
        assertTrue(droppedBlock is ControllerBlock && droppedBlock.visible)
        assertEquals(droppedAt, droppedBlock.position)
    }
    
    @Test
    fun `when drag drop from graph should remove block`() {
        val block = setupMockingControllerBlock()
        val dropEvent = createControllerDragEvent(status = DRAG_DROP, to = CONTROLLERS_HUB, from = GRAPH)
    
        setupResolveIdentifier(dropEvent)
        setupResolveBlock(dropEvent, block)
    
        events.onNext(dropEvent)
    
        val blocks = viewModel.blocks.value
        assertNotNull(blocks)
        
        assertNull(blocks[blockId])
    }
    
    @Test
    fun `when create dependency should update dependencies with added item`() {
        events.onNext(DependencyEvent(
            id = dependencyId,
            startId = blockId,
            status = DEPENDENCY_START,
            rawEndPosition = position1_1
        ))
        
        val dependency = assertHasDependency()
        assertEquals(blockId, dependency.startBlock)
        assertEquals(position1_1, dependency.rawEndPosition)
    }
    
    private fun assertHasDependency(id: String = dependencyId): DependencyState {
        val dependencies = viewModel.dependencies.value
        assertNotNull(dependencies)
        
        val dependency = dependencies[id]
        assertNotNull(dependency)
        
        return dependency
    }
    
    @Test
    fun `when dependency move should update dependencies with new position`() {
        events.onNext(DependencyEvent(
            id = dependencyId,
            startId = blockId,
            status = DEPENDENCY_MOVE,
            rawEndPosition = Position(22f, 22f)
        ))
    
        val dependency = assertHasDependency()
        assertEquals(Position(22f, 22f), dependency.rawEndPosition)
    }
    
    
    @Test
    fun `when save should serialize block types, ids and positions`() {
    
    }
    
    @Test
    fun `when end creating dependency on graph then should cancel`() {
    
    }
    
    @Test
    fun `when end creating dependency on other block should start setup of dependency`() {
    
    }
    
    @Test
    fun `when remove block with dependency should remove associated dependencies`() {
    
    }
    
}