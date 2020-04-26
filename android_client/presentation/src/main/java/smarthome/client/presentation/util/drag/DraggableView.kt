package smarthome.client.presentation.util.drag

import android.view.GestureDetector
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.presentation.util.LongPressGestureDetectorListener
import smarthome.client.presentation.util.extensions.rawPosition
import smarthome.client.presentation.util.rawPosition
import smarthome.client.util.Position
import smarthome.client.util.emptyPosition

open class DraggableView(private val view: View,
                         private val touchHandler: View,
                         private val trigger: DraggableTrigger
) : Draggable, KoinComponent {
    private val draggableHostHolder by inject<DraggableHostHolder>()
    
    override var host: DraggableHost? = null
        set(value) {
            field = value
            
            stableRawPosition = view.rawPosition
        }
    override var possibleHosts: () -> List<DraggableHost> = draggableHostHolder::get
    override var stableRawPosition = emptyPosition
    override var currentRawPosition = emptyPosition
    override var touchPosition = emptyPosition
    private var isDragging: Boolean = false
    private val longPressListener = LongPressGestureDetectorListener()
    private var detector = GestureDetector(view.context, longPressListener)
    private val events = PublishSubject.create<DraggableEvent>()
    
    init {
        if (trigger == DraggableTrigger.LONG_PRESS) {
            longPressListener.onLongPressListener {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                isDragging = true
                true
            }
        }
        
        setTouchHandler(touchHandler)
    }
    
    override fun observeEvents(): Observable<DraggableEvent> {
        return events
    }
    
    final override fun setTouchHandler(view: View) {
        view.setOnTouchListener { _, event ->
            detector.onTouchEvent(event)
            
            when (event.action) {
                MotionEvent.ACTION_DOWN -> handleDownEvent(view, event)
                MotionEvent.ACTION_MOVE -> if (isDragging) handleMoveEvent(event) else false
                MotionEvent.ACTION_UP -> if (isDragging) handleUpEvent() else false
                else -> false
            }
        }
    }
    
    private fun handleDownEvent(view: View, event: MotionEvent): Boolean {
        initializeTouchPosition(view, event)
        
        if (trigger == DraggableTrigger.TOUCH) {
            isDragging = true
            setCurrentPosition(event.rawPosition)
            host?.onStartedDragging(this)
        }
        return true
    }
    
    private fun handleMoveEvent(event: MotionEvent): Boolean {
        setCurrentPosition(event.rawPosition)
        
        host?.onMovedDraggable(this)
        events.onNext(DraggableEvent.MOVE)
        return true
    }
    
    private fun handleUpEvent(): Boolean {
        when (val newHost = findNewHost()) {
            null -> cancelDrag()
            host -> finishDragInsideHost()
            else -> finishDragOutsideHost(newHost)
        }
        
        isDragging = false
        return true
    }
    
    private fun cancelDrag() {
        currentRawPosition = stableRawPosition
        host?.onCancel(this, stableRawPosition)
        events.onNext(DraggableEvent.MOVE)
    }
    
    private fun finishDragInsideHost() {
        stableRawPosition = currentRawPosition
        host?.onFinishMovingDraggableInsideHost(this)
    }
    
    private fun finishDragOutsideHost(newHost: DraggableHost) {
        stableRawPosition = currentRawPosition
        host?.onRemove(this)
        newHost.onAdd(this)
    }
    
    private fun findNewHost() = possibleHosts().find { it.hitTest(currentRawPosition) }
    
    private fun initializeTouchPosition(view: View, event: MotionEvent) {
        val touchX = (view.x + event.x).toInt()
        val touchY = (view.y + event.y).toInt()
        touchPosition = Position(touchX, touchY)
    }
}