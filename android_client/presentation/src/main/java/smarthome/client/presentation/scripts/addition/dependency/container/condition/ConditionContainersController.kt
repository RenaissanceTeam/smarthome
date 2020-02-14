package smarthome.client.presentation.scripts.addition.dependency.container.condition

import com.airbnb.epoxy.TypedEpoxyController
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerModelsHolder
import smarthome.client.presentation.scripts.addition.dependency.container.dependencyUnitContainer

class ConditionContainersController : TypedEpoxyController<List<ContainerModelsHolder>>() {
    
    override fun buildModels(data: List<ContainerModelsHolder>) {
        data.forEach { conditionContainerState ->
            dependencyUnitContainer {
                id(conditionContainerState.id.hashCode())
                dependencyUnitModels(conditionContainerState.models)
            }
        }
    }
}