package smarthome.client.arduino.scripts.presentation.dht

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.scripts.entity.condition.HumidityCondition
import smarthome.client.domain.api.scripts.usecases.dependency.ChangeSetupDependencyConditionUseCase
import smarthome.client.presentation.scripts.setup.dependency.condition.controller.ControllerConditionValueViewModel_

class HumidityConditionModelFactory(
    private val changeSetupDependencyConditionUseCase: ChangeSetupDependencyConditionUseCase
) {
    fun create(humidity: HumidityCondition): EpoxyModel<*> {

        return ControllerConditionValueViewModel_().apply {
            id(humidity.id.hashCode())
            title("Humidity")
            sign(humidity.sign)
            value(humidity.value)
            onSignChanged { newSign ->
                val id = humidity.id ?: return@onSignChanged
                changeSetupDependencyConditionUseCase.execute(id) {
                    (it as? HumidityCondition)?.copy(sign = newSign) ?: it
                }
            }
            
            onValueChanged { newValue ->
                val id = humidity.id ?: return@onValueChanged
                
                changeSetupDependencyConditionUseCase.execute(id) {
                    (it as? HumidityCondition)?.copy(value = newValue) ?: it
                }
            }
        }
    }
}