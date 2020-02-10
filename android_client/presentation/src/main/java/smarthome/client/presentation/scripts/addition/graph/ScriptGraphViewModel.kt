package smarthome.client.presentation.scripts.addition.graph

import org.koin.core.inject
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.presentation.scripts.addition.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.addition.graph.events.navigation.NavigationEvent
import smarthome.client.presentation.scripts.addition.graph.events.navigation.OpenSetupDependency
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationParamLiveData

class ScriptGraphViewModel : KoinViewModel() {
    val setupDependency = NavigationParamLiveData<DependencyId>()
    private val eventBus: GraphEventBus by inject()
    
    init {
        disposable.add(eventBus.observe()
            .filter { it is NavigationEvent }
            .map { it as NavigationEvent }
            .subscribe { event ->
                when (event) {
                    is OpenSetupDependency -> setupDependency.trigger(event.id)
                }
            })
    }
}
