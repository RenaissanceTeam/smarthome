package smarthome.client.arduino.scripts.presentation.dht

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.scripts.entity.condition.TemperatureCondition
import smarthome.client.domain.api.scripts.usecases.dependency.ChangeSetupDependencyConditionUseCase
import smarthome.client.presentation.scripts.setup.dependency.condition.controller.ControllerConditionValueViewModel_

class TemperatureConditionModelFactory(
    private val changeSetupDependencyConditionUseCase: ChangeSetupDependencyConditionUseCase
) {
    fun create(temperature: TemperatureCondition): EpoxyModel<*> {

        return ControllerConditionValueViewModel_().apply {
            id(temperature.id.hashCode())
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