package smarthome.client.presentation.scripts.addition.controllers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
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
import smarthome.client.presentation.scripts.addition.graph.CONTROLLERS_HUB
import smarthome.client.presentation.scripts.addition.graph.DRAG_DROP
import smarthome.client.presentation.scripts.addition.graph.DRAG_START
import smarthome.client.presentation.scripts.addition.graph.Position
import smarthome.client.presentation.scripts.addition.graph.events.GraphEvent
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.drag.ControllerDragOperationInfo
import smarthome.client.presentation.scripts.addition.graph.events.drag.DragEvent
import smarthome.client.presentation.scripts.addition.graph.events.drag.DragOperationInfo
import smarthome.client.presentation.scripts.addition.graph.events.drag.UNKNOWN

class ControllersViewViewModelTest {
    
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    
    private lateinit var viewModel: ControllersViewViewModel
    private lateinit var getDevicesUseCase: GetGeneralDevicesInfo
    private lateinit var observeControllerUseCase: ObserveControllerUseCase
    private lateinit var eventBus: GraphEventBus
    private lateinit var events: PublishSubject<GraphEvent>
    private val position1_1 = Position(1f, 1f)
    
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        getDevicesUseCase = mock { }
        observeControllerUseCase = mock { }
        events = PublishSubject.create()
        eventBus = mock { on { observe() }.then { events } }
    
        startKoin {
            modules(module {
                single { getDevicesUseCase }
                single { observeControllerUseCase }
                single { eventBus }
            })
        }
        viewModel = ControllersViewViewModel()
    }
    
    @After
    fun tearDown() {
        stopKoin()
    }
    
    @Test
    fun `when drag start from controllers hub should update hidden controllers and emit on devices`() {
        val id = 1L
        
        assertThat(viewModel.shouldShow(id)).isEqualTo(true)
        
        events.onNext(DragEvent(info = ControllerDragOperationInfo(
            id = id,
            status = DRAG_START,
            from = CONTROLLERS_HUB,
            dragTouch = position1_1
        )))
        
        assertThat(viewModel.shouldShow(id)).isEqualTo(false)
    }
    
    @Test
    fun `should not accept types other than controller`() {
        assertThat(viewModel.shouldShow(1)).isEqualTo(true)
        
        events.onNext(DragEvent(info = DragOperationInfo(
            status = DRAG_START,
            from = CONTROLLERS_HUB,
            dragTouch = position1_1
        )))
        
        assertThat(viewModel.shouldShow(1)).isEqualTo(true)
    }
    
    @Test
    fun `should not accept destination or origin other than controller hub`() {
        val id = 1L
        
        assertThat(viewModel.shouldShow(id)).isEqualTo(true)
        events.onNext(DragEvent(ControllerDragOperationInfo(id = id,
            status = DRAG_START,
            from = UNKNOWN,
            to = UNKNOWN,
            dragTouch = position1_1
        )))
        
        assertThat(viewModel.shouldShow(id)).isEqualTo(true)
    }
    
    @Test
    fun `when drop controller should push drop event to event bus with updated status and destination`() {
        val id = 1L
        val info = ControllerDragOperationInfo(id = id, from = CONTROLLERS_HUB,
            status = DRAG_START, dragTouch = position1_1)
        viewModel.onDropped(info)
    
        
        verify(eventBus).addEvent(argThat { this is DragEvent
            && this.info is ControllerDragOperationInfo
            && this.info.to == CONTROLLERS_HUB
            && this.info.status == DRAG_DROP
        })
    }
    
    @Test
    fun `when drop other type should push cancel drop event to event bus`() {
    
    }
    
//    @Test
//    fun `when controller is dragged to graph should update map of dragged controllers`() {
//        viewModel.onDraggedToGraph(2)
//
//        assertThat(viewModel.shouldShow(2)).isEqualTo(false)
//        assertThat(viewModel.shouldShow(1)).isEqualTo(true)
//    }
}