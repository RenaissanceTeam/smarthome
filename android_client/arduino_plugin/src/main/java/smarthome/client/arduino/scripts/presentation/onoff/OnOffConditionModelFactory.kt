package smarthome.client.arduino.scripts.presentation.onoff

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.scripts.entity.action.on
import smarthome.client.arduino.scripts.entity.condition.OnOffCondition
import smarthome.client.domain.api.scripts.usecases.dependency.ChangeSetupDependencyConditionUseCase

class OnOffConditionModelFactory(
        private val changeSetupDependencyConditionUseCase: ChangeSetupDependencyConditionUseCase
) {
    fun create(condition: OnOffCondition): EpoxyModel<*> {
        return OnOffConditionViewModel_().apply {
            id(condition.id.hashCode())

            value(condition.value ?: on)
            onChange { newState ->
                changeSetupDependencyConditionUseCase.execute(condition.id) {
                    (it as? OnOffCondition)?.copy(value = newState) ?: it
                }
            }
        }
    }
}