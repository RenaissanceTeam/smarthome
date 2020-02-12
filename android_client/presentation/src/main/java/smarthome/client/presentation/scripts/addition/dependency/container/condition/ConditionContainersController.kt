package smarthome.client.presentation.scripts.addition.dependency.container.condition

import com.airbnb.epoxy.TypedEpoxyController
import smarthome.client.presentation.scripts.addition.dependency.container.dependencyUnitContainer

class ConditionContainersController : TypedEpoxyController<List<ContainerState>>() {
    
    override fun buildModels(data: List<ContainerState>) {
        data.forEach { conditionContainerState ->
            dependencyUnitContainer {
                id(conditionContainerState.id.hashCode())
//                dependencyUnitModels(conditionContainerState.conditions)
            }
        }
    }
}