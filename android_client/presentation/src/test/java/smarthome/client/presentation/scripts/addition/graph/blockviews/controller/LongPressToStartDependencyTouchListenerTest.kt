package smarthome.client.presentation.scripts.addition.graph.blockviews.controller

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.nhaarman.mockitokotlin2.*
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import smarthome.client.presentation.scripts.addition.graph.events.EventPublisher
import smarthome.client.presentation.scripts.addition.graph.events.GraphEvent
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_END
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_MOVE
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_START
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DependencyEvent
import smarthome.client.presentation.scripts.addition.graph.identifier.ControllerGraphBlockIdentifier
import smarthome.client.presentation.util.LongPressGestureDetectorListener

class LongPressToStartDependencyTouchListenerTest {
    
    private lateinit var listener: LongPressToStartDependencyTouchListener
    private lateinit var view: View
    private lateinit var gesture: GestureDetector
    private lateinit var events: PublishSubject<GraphEvent>
    private lateinit var eventBus: GraphEventBus
    private lateinit var eventPublisher: EventPublisher
    private lateinit var longPressListener: LongPressGestureDetectorListener
    private val blockId = ControllerGraphBlockIdentifier(1L)
    
    @Before
    fun setUp() {
        
        view = mock {}
        gesture = mock {}
        eventPublisher = mock {}
        events = PublishSubject.create()
        longPressListener = LongPressGestureDetectorListener()
        eventBus = mock { on { observe() }.then { events } }
        
        listener = LongPressToStartDependencyTouchListener(
            id = blockId,
            blockView = view,
            longPressListener = longPressListener,
            detector = gesture,
            eventPublisher = eventPublisher
        )
    }
    
    
    @Test
    fun `when long press on block then start drag`() {
        gestureDetectorDetectsLongPress(createPressEvent(1f, 2f))
        
        verify(eventPublisher).publish(argThat {
            this is DependencyEvent
                && this.startId == blockId
                && this.status == DEPENDENCY_START
                && this.rawEndPosition.x == 1f
                && this.rawEndPosition.y == 2f
        })
    }
    
    private fun createPressEvent(x: Float, y: Float): MotionEvent {
        return mock {
            on { rawX }.then { x }
            on { rawY }.then { y }
        }
    }
    
    private fun gestureDetectorDetectsLongPress(e: MotionEvent) {
        longPressListener.onLongPress(e)
    }
    
    @Test
    fun `when move after start drag should post move event`() {
        gestureDetectorDetectsLongPress(createPressEvent(1f, 2f))
        
        val moveEvent = createMotionEvent(MotionEvent.ACTION_MOVE, 11f, 22f)
        
        listener.onTouch(view, moveEvent)
        
        verify(eventPublisher).publish(argThat {
            this is DependencyEvent
                && this.startId == blockId
                && this.status == DEPENDENCY_MOVE
                && this.rawEndPosition.x == 11f
                && this.rawEndPosition.y == 22f
        })
    }
    
    private fun createMotionEvent(status: Int, x: Float, y: Float): MotionEvent {
        return mock {
            on { action }.then { status }
            on { rawX }.then { x }
            on { rawY }.then { y }
        }
    }
    
    @Test
    fun `when move without start drag should ignore`() {
        val moveEvent = createMotionEvent(MotionEvent.ACTION_MOVE, 11f, 22f)
    
        listener.onTouch(view, moveEvent)
        
        verifyNoMoreInteractions(eventPublisher)
    }
    
    @Test
    fun `when drop without starting dependency should ignore`() {
        val dropEvent = createMotionEvent(MotionEvent.ACTION_UP, 11f, 22f)
        listener.onTouch(view, dropEvent)
    
        verifyNoMoreInteractions(eventPublisher)
    }
    
    @Test
    fun `when cancel drag should emit end event`() {
        gestureDetectorDetectsLongPress(createPressEvent(1f, 2f))
        verify(eventPublisher).publish(any())
        
        val dropEvent = createMotionEvent(MotionEvent.ACTION_UP, 11f, 22f)
        listener.onTouch(view, dropEvent)
        
        verify(eventPublisher).publish(argThat {
            this is DependencyEvent
                && this.status == DEPENDENCY_END
                && this.rawEndPosition.x == 11f
                && this.rawEndPosition.y == 22f
        })
    }
    
    @Test
    fun `when move after drop should ignore`() {
        gestureDetectorDetectsLongPress(createPressEvent(1f, 2f))
        val dropEvent = createMotionEvent(MotionEvent.ACTION_UP, 11f, 22f)
        listener.onTouch(view, dropEvent)
        verify(eventPublisher, times(2)).publish(any())
        
        
        val moveEvent = createMotionEvent(MotionEvent.ACTION_MOVE, 11f, 22f)
        listener.onTouch(view, moveEvent)
        
        verifyNoMoreInteractions(eventPublisher)
    }
    
    @Test
    fun `when dependency accepted should trigger on create new dependency`() {
    
    }
    
    @Test
    fun `when dependency rejected should remove moving dependency`() {
    
    }
}