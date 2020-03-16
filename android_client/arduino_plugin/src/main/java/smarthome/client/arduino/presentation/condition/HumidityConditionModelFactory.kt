package smarthome.client.arduino.presentation.condition

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.entity.condition.HumidityConditionData
import smarthome.client.domain.api.scripts.usecases.dependency.ChangeSetupDependencyConditionUseCase
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.addition.dependency.condition.ControllerConditionValueViewModel_

class HumidityConditionModelFactory(
    private val changeSetupDependencyConditionUseCase: ChangeSetupDependencyConditionUseCase
) {
    fun create(condition: Condition): EpoxyModel<*> {
        val data = condition.data as? HumidityConditionData
            ?: throw IllegalStateException("creating humidity condition model with wrong condition $condition")
        
        return ControllerConditionValueViewModel_().apply {
            id(condition.id.hashCode())
            title("Humidity")
            sign(data.sign)
            value(data.value)
            onSignChanged { newSign ->
                changeSetupDependencyConditionUseCase.execute(condition.id) {
                    it.copy(data = (it.data as HumidityConditionData).copy(sign = newSign))
                }
            }
    
            onValueChanged { newValue ->
                changeSetupDependencyConditionUseCase.execute(condition.id) {
                    it.copy(data = (it.data as HumidityConditionData).copy(value = newValue))
                }
            }
        }
    }
}