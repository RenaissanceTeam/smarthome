package smarthome.client.presentation.scripts.addition.graph

import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.DROPPED
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.DependencyState
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.MOVING
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.MovingDependencyTipStatus
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_END
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_MOVE
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_START
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DependencyEvent

class DependencyEventsHandlerImpl(
    val emitDependencies: (MutableMap<String, DependencyState>) -> Unit,
    val getCurrentDependencies: () -> MutableMap<String, DependencyState>,
    val emitTipStatus: (MovingDependencyTipStatus) -> Unit,
    val getCurrentTipStatus: () -> MovingDependencyTipStatus
) : DependencyEventsHandler {
    
    override fun handle(event: DependencyEvent) {
        when (event.status) {
            DEPENDENCY_START -> onStartDependency(event)
            DEPENDENCY_MOVE -> moveDependency(event)
            DEPENDENCY_END -> onEndDependency(event)
        }
    }
    
    private fun onStartDependency(event: DependencyEvent) {
        val dependencies = getCurrentDependencies()
        
        val newDependencyState =
            DependencyState(
                id = event.id,
                startBlock = event.startId,
                rawEndPosition = event.rawEndPosition)
        
        dependencies[event.id] = newDependencyState
        emitDependencies(dependencies)
    }
    
    private fun onEndDependency(event: DependencyEvent) {
        emitTipStatus(getCurrentTipStatus().copy(
            status = DROPPED,
            rawPosition = event.rawEndPosition)
        )
    }
    
    private fun moveDependency(event: DependencyEvent) {
        emitMovedDependency(event)
    
        emitTipStatus(
            MovingDependencyTipStatus(
                status = MOVING,
                dependencyId = event.id,
                from = event.startId,
                rawPosition = event.rawEndPosition
            ))
    }
    
    private fun emitMovedDependency(event: DependencyEvent) {
        val dependencies = getCurrentDependencies()
        
        val newDependencyState = getOrCreateDependency(event.id)
            .copy(rawEndPosition = event.rawEndPosition)
        
        dependencies[event.id] = newDependencyState
        emitDependencies(dependencies)
    }
    
    private fun getOrCreateDependency(id: String): DependencyState {
        return getCurrentDependencies()[id] ?: DependencyState(
            id = id)
    }
}