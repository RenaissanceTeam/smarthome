package smarthome.client.presentation.scripts.addition.dependency.container

import com.airbnb.epoxy.TypedEpoxyController
import smarthome.client.presentation.scripts.addition.dependency.condition.ConditionContainerState

class ConditionContainersController : TypedEpoxyController<List<ConditionContainerState>>() {
    
    override fun buildModels(data: List<ConditionContainerState>) {
        data.forEach { conditionContainerState ->
            dependencyUnitContainer {
                id(conditionContainerState.index)
                dependencyUnitModels(conditionContainerState.conditions)
            }
        }
    }
}