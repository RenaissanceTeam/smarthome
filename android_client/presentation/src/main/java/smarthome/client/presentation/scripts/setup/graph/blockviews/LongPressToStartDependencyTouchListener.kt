package smarthome.client.presentation.scripts.setup.graph.blockviews

import android.view.GestureDetector
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import smarthome.client.presentation.scripts.setup.graph.events.EventPublisher
import smarthome.client.presentation.scripts.setup.graph.events.dependency.DEPENDENCY_END
import smarthome.client.presentation.scripts.setup.graph.events.dependency.DEPENDENCY_MOVE
import smarthome.client.presentation.scripts.setup.graph.events.dependency.DEPENDENCY_START
import smarthome.client.presentation.scripts.setup.graph.events.dependency.DependencyEvent
import smarthome.client.presentation.util.LongPressGestureDetectorListener
import smarthome.client.presentation.util.extensions.rawPosition
import smarthome.client.util.generateId
import smarthome.client.util.log

fun setupLongPressToStartDependency(
        uuid: String,
        view: View,
        eventPublisher: EventPublisher,
        touches: Observable<MotionEvent>,
        listenerDisposable: CompositeDisposable,
        block: LongPressToStartDependencyTouchListener.() -> Unit = {}
): LongPressToStartDependencyTouchListener {
    val listener = LongPressGestureDetectorListener()
    val detector = GestureDetector(view.context, listener)

    return LongPressToStartDependencyTouchListener(
            startId = uuid,
            blockView = view,
            longPressListener = listener,
            detector = detector,
            toDispose = listenerDisposable,
            touches = touches,
            eventPublisher = eventPublisher
    ).apply {
        block()
    }
}

class LongPressToStartDependencyTouchListener(
        private val startId: String,
        private val blockView: View,
        private val touches: Observable<MotionEvent>,
        private val toDispose: CompositeDisposable,
        private val longPressListener: LongPressGestureDetectorListener,
        private val detector: GestureDetector,
        private val eventPublisher: EventPublisher
) {
    private var isDependencyMoving = false
    private var movingString: String? = null
    var onStartDependency: (event: DependencyEvent) -> Unit = {}

    init {
        longPressListener.onLongPressListener { event ->
            blockView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)

            startDependency(event)
            true
        }

        toDispose.add(touches.subscribe { event ->
            detector.onTouchEvent(event)

            when (event.action) {
                MotionEvent.ACTION_UP -> endMoving(event)
                MotionEvent.ACTION_MOVE -> moveDependency(event)
            }
        })
    }

    private fun startDependency(event: MotionEvent) {
        isDependencyMoving = true

        val startEvent = DependencyEvent(
                id = getOrCreateMovingId(),
                status = DEPENDENCY_START,
                startId = startId,
                rawEndPosition = event.rawPosition
        )

        onStartDependency(startEvent)
        eventPublisher.publish(startEvent)
    }

    private fun getOrCreateMovingId(): String {
        return movingString ?: generateId().also { movingString = it }
    }

    private fun moveDependency(event: MotionEvent): Boolean {
        if (!isDependencyMoving) return false

        eventPublisher.publish(DependencyEvent(
                id = getOrCreateMovingId(),
                status = DEPENDENCY_MOVE,
                startId = startId,
                rawEndPosition = event.rawPosition
        ))

        return true
    }

    private fun endMoving(event: MotionEvent): Boolean {
        if (!isDependencyMoving) return false

        eventPublisher.publish(DependencyEvent(
                id = getOrCreateMovingId(),
                status = DEPENDENCY_END,
                startId = startId,
                rawEndPosition = event.rawPosition
        ))

        isDependencyMoving = false
        movingString = null
        return true
    }
}