package smarthome.client.presentation.scripts.addition.graph.blockviews.controller

import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import smarthome.client.presentation.scripts.addition.graph.events.EventPublisher
import smarthome.client.presentation.scripts.addition.graph.events.GraphEvent
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
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
        
        val moveEvent = createMoveEvent(11f, 22f)
        
        listener.onTouch(view, moveEvent)
        
        verify(eventPublisher).publish(argThat {
            this is DependencyEvent
                && this.startId == blockId
                && this.status == DEPENDENCY_MOVE
                && this.rawEndPosition.x == 11f
                && this.rawEndPosition.y == 22f
        })
    }
    
    private fun createMoveEvent(x: Float, y: Float): MotionEvent {
        return mock {
            on { action }.then { MotionEvent.ACTION_MOVE }
            on { rawX }.then { x }
            on { rawY }.then { y }
        }
    }
    
    @Test
    fun `when move without start drag should ignore`() {
    
    }
    
    
    @Test
    fun `when cancel drag should emit end event`() {
    
    }
    
    @Test
    fun `when dependency accepted should trigger on create new dependency`() {
    
    }
    
    @Test
    fun `when dependency rejected should remove moving dependency`() {
    
    }
}