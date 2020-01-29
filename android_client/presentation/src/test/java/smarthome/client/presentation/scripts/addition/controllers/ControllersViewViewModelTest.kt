package smarthome.client.presentation.scripts.addition.controllers

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
import smarthome.client.domain.api.devices.usecase.GetGeneralDevicesInfo
import smarthome.client.entity.Controller
import smarthome.client.presentation.scripts.addition.graph.*
import smarthome.client.presentation.scripts.addition.graph.events.GraphEvent
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.drag.*
import smarthome.client.util.DataStatus

class ControllersViewViewModelTest {
    
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    
    private lateinit var viewModel: ControllersHubViewModel
    private lateinit var getDevicesUseCase: GetGeneralDevicesInfo
    private lateinit var observeControllerUseCase: ObserveControllerUseCase
    private lateinit var eventBus: GraphEventBus
    private lateinit var events: PublishSubject<GraphEvent>
    private val position1_1 = Position(1f, 1f)
    
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        getDevicesUseCase = mock { }
        observeControllerUseCase =
            mock { on { execute(any()) }.then { Observable.empty<DataStatus<Controller>>() } }
        events = PublishSubject.create()
        eventBus = mock { on { observe() }.then { events } }
    
        startKoin {
            modules(module {
                single { getDevicesUseCase }
                single { observeControllerUseCase }
                single { eventBus }
            })
        }
        viewModel = ControllersHubViewModel()
    }
    
    @After
    fun tearDown() {
        stopKoin()
    }
    
    @Test
    fun `when drag start from controllers hub should update hidden controllers and emit on devices`() {
        val id = 1L
        
        assertThat(viewModel.shouldShow(id)).isEqualTo(true)
    
        events.onNext(ControllerDragEvent(
            id = id,
            dragInfo = CommonDragInfo(
                status = DRAG_START,
                from = CONTROLLERS_HUB,
                dragTouch = position1_1
            ))
        )
        
        assertThat(viewModel.shouldShow(id)).isEqualTo(false)
    }
    
    @Test
    fun `should not accept destination or origin other than controller hub`() {
        val id = 1L
        
        assertThat(viewModel.shouldShow(id)).isEqualTo(true)
        events.onNext(ControllerDragEvent(
            id = id,
            dragInfo = CommonDragInfo(
                status = DRAG_START,
                from = UNKNOWN,
                to = UNKNOWN,
                dragTouch = position1_1
            ))
        )
    
        assertThat(viewModel.shouldShow(id)).isEqualTo(true)
    }
    
    @Test
    fun `when drop controller should push drop event to event bus with updated status and destination`() {
        val id = 1L
        val info = ControllerDragEvent(
            id = id,
            dragInfo = CommonDragInfo(
                status = DRAG_START,
                from = CONTROLLERS_HUB,
                dragTouch = position1_1
            )
        )
        
        viewModel.onDropped(info)
    
    
        verify(eventBus).addEvent(argThat {
            this is ControllerDragEvent
                && this.dragInfo.to == CONTROLLERS_HUB
                && this.dragInfo.status == DRAG_DROP
        })
    }
    
    @Test
    fun `when drop other type should push cancel drop event to event bus`() {
        val copiedEvent = mock<GraphDragEvent>()
        val otherDragEvent = mock<GraphDragEvent> {
            on { dragInfo }.then { CommonDragInfo(from = CONTROLLERS_HUB,
                status = DRAG_START, dragTouch = position1_1) }
            on { copyWithDragInfo(any()) }.then { copiedEvent }
        }
        
        viewModel.onDropped(otherDragEvent)
        
        verify(otherDragEvent).copyWithDragInfo(argThat { this.status == DRAG_CANCEL
            && this.to == CONTROLLERS_HUB
        })
        verify(eventBus).addEvent(copiedEvent)
    }
    
    @Test
    fun `when controller is dropped to hub it should be observed`() {
        val id = 123L
        
        events.onNext(ControllerDragEvent(id, dragInfo = CommonDragInfo(
            status = DRAG_DROP,
            from = "ANYWHERE",
            to = CONTROLLERS_HUB,
            dragTouch = position1_1
        )))
        
        verify(observeControllerUseCase).execute(id)
    }
    
    @Test
    fun `when controller is already observed and dropped to hub should be no calls to observe UC`() {
        val id = 123L
        
        
        events.onNext(ControllerDragEvent(id, dragInfo = CommonDragInfo(
            status = DRAG_DROP,
            from = "ANYWHERE",
            to = CONTROLLERS_HUB,
            dragTouch = position1_1
        )))
        
        verify(observeControllerUseCase).execute(id)
        
        events.onNext(ControllerDragEvent(id, dragInfo = CommonDragInfo(
            status = DRAG_DROP,
            from = CONTROLLERS_HUB,
            to = CONTROLLERS_HUB,
            dragTouch = position1_1
        )))
        
        verifyNoMoreInteractions(observeControllerUseCase)
    }
    
    @Test
    fun `when drag started should add to event bus`() {
        val id = 123L
        viewModel.onDragStarted(id, position1_1)
        
        verify(eventBus).addEvent(argThat {
            this is ControllerDragEvent
                && this.id == id
                && this.dragInfo.status == DRAG_START
                && this.dragInfo.from == CONTROLLERS_HUB
                && this.dragInfo.dragTouch == position1_1
        })
    }
}