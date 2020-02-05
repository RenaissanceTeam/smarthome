package smarthome.client.presentation.scripts.addition.graph

import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.DROPPED
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.MOVING
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.MovingDependency
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_END
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_MOVE
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_START
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DependencyEvent

class DependencyEventsHandlerImpl(
    val emitMovingDependency: (MovingDependency) -> Unit,
    val getCurrentMovingDependency: () -> MovingDependency?
) : DependencyEventsHandler {
    
    override fun handle(event: DependencyEvent) {
        when (event.status) {
            DEPENDENCY_START -> onStartDependency(event)
            DEPENDENCY_MOVE -> moveDependency(event)
            DEPENDENCY_END -> onEndDependency(event)
        }
    }
    
    private fun onStartDependency(event: DependencyEvent) {
        emitMovingDependency(MovingDependency(
            id = event.id,
            status = MOVING,
            startBlock = event.startId,
            rawEndPosition = event.rawEndPosition))
    }
    
    private fun onEndDependency(event: DependencyEvent) {
        getCurrentMovingDependency()?.apply {
            emitMovingDependency(copy(
                status = DROPPED,
                rawEndPosition = event.rawEndPosition
            ))
        }
    }
    
    private fun moveDependency(event: DependencyEvent) {
        getCurrentMovingDependency()?.apply {
            emitMovingDependency(copy(
                status = MOVING,
                rawEndPosition = event.rawEndPosition

            ))
        }
    }
}
