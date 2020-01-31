package smarthome.client.presentation.scripts.addition.graph

import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.DependencyState
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_MOVE
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DEPENDENCY_START
import smarthome.client.presentation.scripts.addition.graph.events.dependency.DependencyEvent

class DependencyEventsHandler(
    val emitDependencies: (MutableMap<String, DependencyState>) -> Unit,
    val getCurrentDependencies: () -> MutableMap<String, DependencyState>
) {
    fun handle(event: DependencyEvent) {
        when (event.status) {
            DEPENDENCY_START -> onStartDependency(event)
            DEPENDENCY_MOVE -> moveDependency(event)
        }
    }
    
    private fun onStartDependency(event: DependencyEvent) {
        val dependencies = getCurrentDependencies()
        
        val newDependencyState = DependencyState(id = event.id,
            startBlock = event.startId,
            rawEndPosition = event.rawEndPosition)
        
        dependencies[event.id] = newDependencyState
        emitDependencies(dependencies)
    }
    
    private fun moveDependency(event: DependencyEvent) {
        val dependencies = getCurrentDependencies()
    
        val newDependencyState = getOrCreateDependency(event.id)
            .copy(rawEndPosition = event.rawEndPosition)
        
        dependencies[event.id] = newDependencyState
        emitDependencies(dependencies)
    }
    
    private fun getOrCreateDependency(id: String): DependencyState {
        return getCurrentDependencies()[id] ?: DependencyState(id = id)
    }
}