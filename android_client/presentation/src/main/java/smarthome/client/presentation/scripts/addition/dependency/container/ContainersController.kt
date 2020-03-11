package smarthome.client.presentation.scripts.addition.dependency.container

import com.airbnb.epoxy.Typed2EpoxyController
import smarthome.client.entity.script.dependency.DependencyUnit
import smarthome.client.presentation.scripts.addition.dependency.SetupDependencyViewModel

class ContainersController<T : DependencyUnit>(
    private val modelResolver: ContainerModelResolver<T>
) : Typed2EpoxyController<List<ContainerState<T>>, SetupDependencyViewModel>() {
    
    override fun buildModels(data: List<ContainerState<T>>, viewModel: SetupDependencyViewModel) {
        data.forEach { state ->
            dependencyUnitContainer {
                id(state.id.hashCode())
                dependencyUnitModels(state.data.map { modelResolver.resolve(it) })
                selectionMode(state.selectionMode)
                select(state.isSelected)
                onSelect { viewModel.onSelect(state.id, it) }
            }
        }
    }
}