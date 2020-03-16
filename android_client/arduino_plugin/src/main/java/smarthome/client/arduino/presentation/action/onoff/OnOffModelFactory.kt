package smarthome.client.arduino.presentation.action.onoff

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.entity.action.OnOffActionData
import smarthome.client.domain.api.scripts.usecases.dependency.ChangeSetupDependencyActionUseCase
import smarthome.client.entity.script.dependency.action.Action

class OnOffModelFactory(
    private val changeSetupDependencyActionUseCase: ChangeSetupDependencyActionUseCase
) {
    fun create(action: Action): EpoxyModel<*> {
        val data = action.data as? OnOffActionData
            ?: throw IllegalStateException("can't create onoff action model with action $action")
        return OnOffActionViewModel_().apply {
            id(action.id.hashCode())
            state(data.value)
            onChangeState { newValue ->
                changeSetupDependencyActionUseCase.execute(action.id) {
                    it.copy(data = (it.data as OnOffActionData).copy(value = newValue))
                }
            }
        }
    }
}