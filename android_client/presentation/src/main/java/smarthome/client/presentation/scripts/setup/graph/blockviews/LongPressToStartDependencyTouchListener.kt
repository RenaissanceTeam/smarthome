package smarthome.client.presentation.scripts.setup.graph.blockviews

import android.view.GestureDetector
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import com.jakewharton.rxbinding3.view.touches
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.SimpleDependencyId
import smarthome.client.presentation.scripts.setup.graph.events.EventPublisher
import smarthome.client.presentation.scripts.setup.graph.events.dependency.DEPENDENCY_END
import smarthome.client.presentation.scripts.setup.graph.events.dependency.DEPENDENCY_MOVE
import smarthome.client.presentation.scripts.setup.graph.events.dependency.DEPENDENCY_START
import smarthome.client.presentation.scripts.setup.graph.events.dependency.DependencyEvent
import smarthome.client.presentation.util.LongPressGestureDetectorListener
import smarthome.client.presentation.util.extensions.rawPosition

fun setupLongPressToStartDependency(
    uuid: String,
    view: View,
    eventPublisher: EventPublisher,
    block: LongPressToStartDependencyTouchListener.() -> Unit = {}
): LongPressToStartDependencyTouchListener {
    val listener = LongPressGestureDetectorListener()
    val detector = GestureDetector(view.context, listener)
    
    return LongPressToStartDependencyTouchListener(
        startId = uuid,
        blockView = view,
        longPressListener = listener,
        detector = detector,
        eventPublisher = eventPublisher
    ).apply {
        block()
    }
}

class LongPressToStartDependencyTouchListener(
    private val startId: String,
    private val blockView: View,
    private val longPressListener: LongPressGestureDetectorListener,
    private val detector: GestureDetector,
    private val eventPublisher: EventPublisher
) {
    private var isDependencyMoving = false
    private var movingDependencyId: DependencyId? = null
    var onStartDependency: (event: DependencyEvent) -> Unit = {}
    
    init {
        longPressListener.onLongPressListener { event ->
            blockView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    
            startDependency(event)
            true
        }
    
        blockView.touches { event ->
            detector.onTouchEvent(event)
    
            when (event.action) {
                MotionEvent.ACTION_UP -> endMoving(event)
                MotionEvent.ACTION_MOVE -> moveDependency(event)
                MotionEvent.ACTION_DOWN -> true
                else -> false
            }
        }.subscribe()
    }
    
    private fun startDependency(event: MotionEvent) {
        isDependencyMoving = true

        val startEvent = DependencyEvent(
            id = getOrCreateMovingDependencyId(),
            status = DEPENDENCY_START,
            startId = startId,
            rawEndPosition = event.rawPosition
        )
        
        onStartDependency(startEvent)
        eventPublisher.publish(startEvent)
    }
    
    private fun getOrCreateMovingDependencyId(): DependencyId {
        return movingDependencyId ?: SimpleDependencyId().also { movingDependencyId = it }
    }
    
    private fun moveDependency(event: MotionEvent): Boolean {
        if (!isDependencyMoving) return false
        
        eventPublisher.publish(DependencyEvent(
            id = getOrCreateMovingDependencyId(),
            status = DEPENDENCY_MOVE,
            startId = startId,
            rawEndPosition = event.rawPosition
        ))
        
        return true
    }
    
    private fun endMoving(event: MotionEvent): Boolean {
        if (!isDependencyMoving) return false
    
        eventPublisher.publish(DependencyEvent(
            id = getOrCreateMovingDependencyId(),
            status = DEPENDENCY_END,
            startId = startId,
            rawEndPosition = event.rawPosition
        ))
        
        isDependencyMoving = false
        movingDependencyId = null
        return true
    }
}