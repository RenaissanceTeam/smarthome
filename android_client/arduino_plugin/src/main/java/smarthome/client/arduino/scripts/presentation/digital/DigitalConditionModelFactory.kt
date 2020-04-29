package smarthome.client.arduino.scripts.presentation.digital

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.scripts.entity.condition.DigitalCondition
import smarthome.client.domain.api.scripts.usecases.dependency.ChangeSetupDependencyConditionUseCase

class DigitalConditionModelFactory(
        private val changeSetupDependencyConditionUseCase: ChangeSetupDependencyConditionUseCase
) {
    fun create(condition: DigitalCondition): EpoxyModel<*> {

        return DigitalConditionViewModel_().apply {
            id(condition.id.hashCode())

            value(condition.value.orEmpty().toIntOrNull() ?: 0)
            onChange { newValue ->
                changeSetupDependencyConditionUseCase.execute(condition.id) {
                    (it as? DigitalCondition)?.copy(value = newValue.toString()) ?: it
                }
            }
        }
    }
}
