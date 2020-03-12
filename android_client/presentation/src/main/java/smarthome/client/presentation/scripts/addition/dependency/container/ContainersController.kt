package smarthome.client.presentation.scripts.addition.dependency.container

import com.airbnb.epoxy.Typed2EpoxyController
import smarthome.client.entity.script.dependency.DependencyUnit
import smarthome.client.presentation.scripts.addition.dependency.SetupDependencyViewModel

class ContainersController<T : DependencyUnit>(
    private val modelResolver: DependencyUnitModelResolver<T>
) : Typed2EpoxyController<List<ContainerState<T>>, SetupDependencyViewModel>() {
    
    override fun buildModels(data: List<ContainerState<T>>, viewModel: SetupDependencyViewModel) {
        data.forEach { state ->
            dependencyUnitContainer {
                id(state.id.hashCode())
                dependencyUnitModels(state.allData.map(modelResolver::resolve))
                selectionMode(state.selectionMode)
                select(state.isSelected)
                onSelect { viewModel.onSelect(state.id, it) }
            }
        }
    }
}