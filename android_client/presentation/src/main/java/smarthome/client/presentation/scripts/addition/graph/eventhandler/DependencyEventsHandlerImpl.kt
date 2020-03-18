package smarthome.client.presentation.scripts.addition.graph.eventhandler

import androidx.lifecycle.MutableLiveData
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.DROPPED
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.MOVING
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.MovingDependency
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.STARTED
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_END
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_MOVE
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_START
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DependencyEvent
import smarthome.client.util.log

class DependencyEventsHandlerImpl(
    val movingDependency: MutableLiveData<MovingDependency>
) : DependencyEventsHandler {
    
    override fun handle(event: DependencyEvent) {
        when (event.status) {
            DEPENDENCY_START -> onStartDependency(event)
            DEPENDENCY_MOVE -> moveDependency(event)
            DEPENDENCY_END -> onEndDependency(event)
        }
    }
    
    private fun onStartDependency(event: DependencyEvent) {
        movingDependency.value = MovingDependency(
            id = event.id,
            status = STARTED,
            startBlock = event.startId,
            rawEndPosition = event.rawEndPosition)
    }
    
    private fun onEndDependency(event: DependencyEvent) {
        movingDependency.value?.apply {
            movingDependency.value = copy(
                status = DROPPED,
                rawEndPosition = event.rawEndPosition
            )
        }
    }
    
    private fun moveDependency(event: DependencyEvent) {
        movingDependency.value?.apply {
            movingDependency.value = copy(
                status = MOVING,
                rawEndPosition = event.rawEndPosition
            )
        }
    }
}
