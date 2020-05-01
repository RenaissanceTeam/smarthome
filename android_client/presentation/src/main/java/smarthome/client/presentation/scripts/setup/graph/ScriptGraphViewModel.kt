package smarthome.client.presentation.scripts.setup.graph

import androidx.lifecycle.MutableLiveData
import org.koin.core.inject
import smarthome.client.domain.api.scripts.usecases.setup.AddBlockUseCase
import smarthome.client.domain.api.scripts.usecases.setup.GetSetupScriptUseCase
import smarthome.client.entity.script.block.NotificationBlock
import smarthome.client.presentation.scripts.setup.graph.events.GraphEventBus
import smarthome.client.presentation.scripts.setup.graph.events.navigation.NavigationEvent
import smarthome.client.presentation.scripts.setup.graph.events.navigation.OpenSetupDependency
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.NavigationParamLiveData
import smarthome.client.util.Position
import smarthome.client.util.generateId

class ScriptGraphViewModel : KoinViewModel() {
    val setupDependency = NavigationParamLiveData<String>()
    private val eventBus: GraphEventBus by inject()
    private val getSetupScriptUseCase: GetSetupScriptUseCase by inject()
    private val addBlockUseCase: AddBlockUseCase by inject()
    val title = MutableLiveData<String>()


    override fun onResume() {
        disposable.add(eventBus.observe()
                .filter { it is NavigationEvent }
                .map { it as NavigationEvent }
                .subscribe { event ->
                    when (event) {
                        is OpenSetupDependency -> setupDependency.trigger(event.id)
                    }
                })

        title.value = getSetupScriptUseCase.execute()?.name.orEmpty()
    }

    override fun onPause() {
        disposable.clear()
    }

    fun onAddNotification(position: Position) {
        addBlockUseCase.execute(NotificationBlock(
                id = generateId(),
                position = (position)
        ))
    }
}
