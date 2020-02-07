package smarthome.client.presentation.scripts.addition.graph.blockviews.controller

import android.view.GestureDetector
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import smarthome.client.entity.script.block.BlockId
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.SimpleDependencyId
import smarthome.client.presentation.util.rawPosition
import smarthome.client.presentation.scripts.addition.graph.events.EventPublisher
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_END
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_MOVE
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_START
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DependencyEvent
import smarthome.client.presentation.util.LongPressGestureDetectorListener

fun setupLongPressToStartDependency(
    id: BlockId,
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
    private val id: BlockId,
    private val blockView: View,
    private val longPressListener: LongPressGestureDetectorListener,
    private val detector: GestureDetector,
    private val eventPublisher: EventPublisher
) : View.OnTouchListener {
    private var isDependencyMoving = false
    private var movingDependencyId: DependencyId? = null
    var onStartDependency: (event: DependencyEvent) -> Unit = {}
    
    init {
    
        blockView.setOnTouchListener(this)
        
        longPressListener.onLongPressListener { event ->
            blockView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    
            startDependency(event)
            true
        }
    }
    
    private fun startDependency(event: MotionEvent) {
        isDependencyMoving = true

        val startEvent = DependencyEvent(
            id = getMovingDependencyId(),
            status = DEPENDENCY_START,
            startId = id,
            rawEndPosition = event.rawPosition
        )
        
        onStartDependency(startEvent)
        eventPublisher.publish(startEvent)
    }
    
    private fun getMovingDependencyId(): DependencyId {
        return movingDependencyId ?: createNewDependencyId().also { movingDependencyId = it }
    }
    
    private fun createNewDependencyId(): DependencyId {
        return SimpleDependencyId()
    }
    
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        detector.onTouchEvent(event)
        return when (event.action) {
            MotionEvent.ACTION_UP -> endMoving(event)
            MotionEvent.ACTION_MOVE -> moveDependency(event)
            MotionEvent.ACTION_DOWN -> true
            else -> false
        }
    }
    
    private fun moveDependency(event: MotionEvent): Boolean {
        if (!isDependencyMoving) return false

        eventPublisher.publish(DependencyEvent(
            id = getMovingDependencyId(),
            status = DEPENDENCY_MOVE,
            startId = id,
            rawEndPosition = event.rawPosition
        ))
        
        return true
    }
    
    private fun endMoving(event: MotionEvent): Boolean {
        if (!isDependencyMoving) return false
    
        eventPublisher.publish(DependencyEvent(
            id = getMovingDependencyId(),
            status = DEPENDENCY_END,
            startId = id,
            rawEndPosition = event.rawPosition
        ))
        
        isDependencyMoving = false
        movingDependencyId = null
        return true
    }
}