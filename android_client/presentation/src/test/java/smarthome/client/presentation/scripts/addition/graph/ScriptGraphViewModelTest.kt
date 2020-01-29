package smarthome.client.presentation.scripts.addition.graph

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
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
import smarthome.client.util.DataStatus

class ScriptGraphViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    
    private lateinit var observeControllerUseCase: ObserveControllerUseCase
    private lateinit var eventBus: GraphEventBus
    private lateinit var events: PublishSubject<GraphEvent>
    private val position1_1 = Position(1f, 1f)
    private lateinit var viewModel: ScriptGraphViewModel
    
    
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        observeControllerUseCase =
            mock { on { execute(any()) }.then { Observable.empty<DataStatus<Controller>>() } }
        events = PublishSubject.create()
        eventBus = mock { on { observe() }.then { events } }
    
        startKoin {
            modules(module {
                single { observeControllerUseCase }
                single { eventBus }
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
    fun `when drop controller from hub should add create view for it`() {
        val id = 123L
//        events.onNext(ControllerDragEvent(id = id, dragInfo = CommonDragInfo(
//            status =
//        )))
    }
    
    
    
    @Test
    fun `when start drag on graph should hide dragged block`() {
    
    }
}