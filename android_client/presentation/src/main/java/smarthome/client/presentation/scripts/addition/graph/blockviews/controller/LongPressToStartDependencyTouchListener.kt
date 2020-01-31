package smarthome.client.presentation.scripts.addition.graph.blockviews.controller

import android.view.GestureDetector
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import smarthome.client.presentation.scripts.addition.graph.events.EventPublisher
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_START
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DependencyEvent
import smarthome.client.presentation.scripts.addition.graph.identifier.GraphBlockIdentifier
import smarthome.client.presentation.util.LongPressGestureDetectorListener
import smarthome.client.presentation.util.Position

fun setupLongPressToStartDependency(
    id: GraphBlockIdentifier,
    view: View,
    eventPublisher: EventPublisher,
    block: LongPressToStartDependencyTouchListener.() -> Unit
): LongPressToStartDependencyTouchListener {
    val listener = LongPressGestureDetectorListener()
    val detector = GestureDetector(view.context, listener)
    
    return LongPressToStartDependencyTouchListener(
        id = id,
        blockView = view,
        longPressListener = listener,
        detector = detector,
        eventPublisher = eventPublisher
    ).apply {
        block()
    }
}

class LongPressToStartDependencyTouchListener(
    private val id: GraphBlockIdentifier,
    private val blockView: View,
    private val longPressListener: LongPressGestureDetectorListener,
    private val detector: GestureDetector,
    private val eventPublisher: EventPublisher
) : View.OnTouchListener {
    
    
    init {
        blockView.setOnTouchListener(this)
        
        longPressListener.onLongPressListener { event ->
            blockView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            sendStartDependencyEvent(event)
            true
        }
    }
    
    private fun sendStartDependencyEvent(event: MotionEvent) {
        eventPublisher.publish(DependencyEvent(
            status = DEPENDENCY_START,
            startId = id,
            rawEndPosition = Position(event.rawX, event.rawY)
        ))
    }
    
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        detector.onTouchEvent(event)
        
        return when (event.action) {
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_MOVE,
            MotionEvent.ACTION_DOWN -> true
            else -> false
        }
    }
}