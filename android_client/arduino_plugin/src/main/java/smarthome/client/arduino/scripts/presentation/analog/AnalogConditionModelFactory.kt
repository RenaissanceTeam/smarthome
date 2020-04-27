package smarthome.client.arduino.scripts.presentation.analog

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.scripts.entity.condition.AnalogCondition
import smarthome.client.domain.api.scripts.usecases.dependency.ChangeSetupDependencyConditionUseCase

class AnalogConditionModelFactory(
        private val changeSetupDependencyConditionUseCase: ChangeSetupDependencyConditionUseCase
) {
    fun create(condition: AnalogCondition): EpoxyModel<*> {

        return AnalogConditionViewModel_().apply {
            id(condition.id.hashCode())

            value(condition.value.orEmpty().toIntOrNull() ?: 0)
            onChange { newValue ->
                changeSetupDependencyConditionUseCase.execute(condition.id) {
                    (it as? AnalogCondition)?.copy(value = newValue.toString()) ?: it
                }
            }

        }
    }
}
