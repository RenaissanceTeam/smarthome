package smarthome.client.arduino.presentation.condition

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.entity.condition.TemperatureConditionData
import smarthome.client.domain.api.scripts.usecases.dependency.ChangeSetupDependencyConditionUseCase
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.setup.dependency.condition.ControllerConditionValueViewModel_

class TemperatureConditionModelFactory(
    private val changeSetupDependencyConditionUseCase: ChangeSetupDependencyConditionUseCase
) {
    fun create(condition: Condition): EpoxyModel<*> {
        val data = condition.data as? TemperatureConditionData
            ?: throw IllegalStateException("creating temperature condition model with wrong condition $condition")
        
        return ControllerConditionValueViewModel_().apply {
            id(condition.id.hashCode())
            title("Temperature")
            sign(data.sign)
            value(data.value)
    
            onSignChanged { newSign ->
                changeSetupDependencyConditionUseCase.execute(condition.id) {
                    it.copy(data = (it.data as TemperatureConditionData).copy(sign = newSign))
                }
            }
    
            onValueChanged { newValue ->
                changeSetupDependencyConditionUseCase.execute(condition.id) {
                    it.copy(data = (it.data as TemperatureConditionData).copy(value = newValue))
                }
            }
        }
    }
}