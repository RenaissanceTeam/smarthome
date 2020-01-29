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
import smarthome.client.presentation.scripts.addition.graph.events.GraphEvent
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.drag.*
import smarthome.client.presentation.scripts.addition.graph.views.state.ControllerBlock
import smarthome.client.presentation.scripts.addition.graph.views.state.GraphBlockResolver
import smarthome.client.util.DataStatus
import kotlin.test.assertNotNull
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
        val id = 12L
        val event = ControllerDragEvent(id, CommonDragInfo(
            status = DRAG_START,
            from = CONTROLLERS_HUB,
            dragTouch = position1_1
        ))
        viewModel.onDropped(event, position1_1)
        verify(eventBus).addEvent(argThat {
            this is ControllerDragEvent
                && this.id == id
                && this.dragInfo.to == GRAPH
                && this.dragInfo.status == DRAG_DROP
        })
    }
    
    @Test
    fun `when drop block should calculate position as drop position minus touch position`() {
        val id = 12L
        val event = ControllerDragEvent(id, CommonDragInfo(
            status = DRAG_START,
            from = CONTROLLERS_HUB,
            dragTouch = position1_1
        ))
        viewModel.onDropped(event, position1_1)
        verify(eventBus).addEvent(argThat {
            this is ControllerDragEvent
                && this.dragInfo.position == Position(0f, 0f)
        })
    }
    
    @Test
    fun `when drop controller from hub should resolve block for it and emit`() {
        val id = 123L
        val blockId = ControllerGraphBlockIdentifier(id)
        val dropEvent = ControllerDragEvent(id = id, dragInfo = CommonDragInfo(
            status = DRAG_DROP,
            to = GRAPH,
            from = CONTROLLERS_HUB,
            position = position1_1
        ))
        whenever(blockResolver.resolveIdentifier(dropEvent)).then { blockId }
        val controllerBlock = mock<ControllerBlock> {
            on { this.id }.then { blockId }
            on { position }.then { position1_1 }
        }
        whenever(controllerBlock.copyWithPosition(any())).then { controllerBlock }
        whenever(blockResolver.createBlock(dropEvent)).then { controllerBlock }

        events.onNext(dropEvent)
        
        val blocks = viewModel.blocks.value
        assertNotNull(blocks)
        
        val addedBlock = blocks[blockId]
        assertNotNull(addedBlock)
        
        assertTrue(addedBlock is ControllerBlock)
        assertThat(addedBlock.id).isEqualTo(blockId)
        assertThat(addedBlock.position).isEqualTo(position1_1)
    }
    
    
    
    @Test
    fun `when start drag on graph should hide dragged block`() {
    
    }
}