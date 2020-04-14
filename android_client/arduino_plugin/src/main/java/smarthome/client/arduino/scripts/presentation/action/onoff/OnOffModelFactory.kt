package smarthome.client.arduino.scripts.presentation.action.onoff

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.scripts.entity.action.OnOffAction
import smarthome.client.domain.api.scripts.usecases.dependency.ChangeSetupDependencyActionUseCase
import smarthome.client.entity.script.dependency.action.Action

class OnOffModelFactory(
    private val changeSetupDependencyActionUseCase: ChangeSetupDependencyActionUseCase
) {
    fun create(action: Action): EpoxyModel<*> {
        val onoff = action as? OnOffAction
            ?: throw IllegalStateException("can't create onoff action model with action $action")
        
        return OnOffActionViewModel_().apply {
            id(onoff.id.hashCode())
            state(onoff.value)
            onChangeState { newValue ->
                val id = onoff.id ?: return@onChangeState
                changeSetupDependencyActionUseCase.execute(id) {
                    (it as? OnOffAction)?.copy(value = newValue) ?: it
                }
            }
        }
    }
}