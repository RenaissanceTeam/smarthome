package smarthome.client.arduino.scripts.presentation.condition

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.scripts.entity.condition.TemperatureCondition
import smarthome.client.domain.api.scripts.usecases.dependency.ChangeSetupDependencyConditionUseCase
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.setup.dependency.condition.ControllerConditionValueViewModel_

class TemperatureConditionModelFactory(
    private val changeSetupDependencyConditionUseCase: ChangeSetupDependencyConditionUseCase
) {
    fun create(condition: Condition): EpoxyModel<*> {
        val temperature = condition as? TemperatureCondition
            ?: throw IllegalStateException("creating temperature condition model with wrong condition $condition")
        
        return ControllerConditionValueViewModel_().apply {
            id(condition.id.hashCode())
            title("Temperature")
            sign(temperature.sign)
            value(temperature.value)
            
            onSignChanged { newSign ->
                val id = temperature.id ?: return@onSignChanged
                changeSetupDependencyConditionUseCase.execute(id) {
                    (it as? TemperatureCondition)?.copy(sign = newSign) ?: it
                }
            }
            
            onValueChanged { newValue ->
                val id = temperature.id ?: return@onValueChanged
                
                changeSetupDependencyConditionUseCase.execute(id) {
                    (it as? TemperatureCondition)?.copy(value = newValue) ?: it
                }
            }
        }
    }
}