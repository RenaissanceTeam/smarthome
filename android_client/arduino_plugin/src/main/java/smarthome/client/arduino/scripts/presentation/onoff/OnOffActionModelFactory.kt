package smarthome.client.arduino.scripts.presentation.onoff

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.scripts.entity.action.OnOffAction
import smarthome.client.domain.api.scripts.usecases.dependency.ChangeSetupDependencyActionUseCase

class OnOffActionModelFactory(
        private val changeSetupDependencyActionUseCase: ChangeSetupDependencyActionUseCase
) {
    fun create(onoff: OnOffAction): EpoxyModel<*> {
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