package smarthome.client.arduino.presentation.condition

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.entity.condition.HumidityConditionData
import smarthome.client.domain.api.scripts.usecases.dependency.ChangeSetupDependencyConditionUseCase
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.addition.dependency.condition.ControllerConditionValueViewModel_
import smarthome.client.util.log

class HumidityConditionModelFactory(
    private val changeSetupDependencyConditionUseCase: ChangeSetupDependencyConditionUseCase
) {
    fun create(condition: Condition): EpoxyModel<*> {
        val data = condition.data as? HumidityConditionData
            ?: throw IllegalStateException("creating humidity condition model with wrong condition $condition")
        
        return ControllerConditionValueViewModel_().apply {
            id("humidity")
            title("Humidity")
            sign(data.sign)
            value(data.value)
            
            onSignChanged {
                copyConditionWith(condition, data.copy(sign = it))
            }
            
            onValueChanged {
                copyConditionWith(condition, data.copy(value = it))
            }
        }
    }
    
    private fun copyConditionWith(condition: Condition, data: HumidityConditionData) {
        changeSetupDependencyConditionUseCase.execute(condition.copy(data = data))
    }
}