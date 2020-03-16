package smarthome.client.presentation.scripts.addition.dependency

import androidx.lifecycle.MutableLiveData
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.domain.api.scripts.usecases.dependency.GetSetupDependencyUseCase
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyUnit
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerId
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerState
import smarthome.client.presentation.util.extensions.updateWith
import smarthome.client.util.containsThat
import smarthome.client.util.findAndModify

class ContainersViewModel<T : DependencyUnit>(
    emptyUnitsCreator: (Long, Dependency) -> List<T>
) : KoinComponent {
    val scriptId = 1L // TODO
    val containersLiveData = MutableLiveData<List<ContainerState<T>>>()
    private val containersState = ContainersStates { emptyUnitsCreator.invoke(scriptId, it) }
    
    fun setData(data: List<T>, dependency: Dependency) {
        containersLiveData.updateWith {
            containersState.apply { setData(data, dependency) }.asList()
        }
    }
    
    fun onSelect(id: ContainerId, isSelected: Boolean) {
        containersLiveData.updateWith {
            it.orEmpty().findAndModify(
                { it.id == id },
                { it.copy(isSelected = isSelected) }
            )
        }
    }
    
    fun contains(id: ContainerId): Boolean {
        return containersState.asList().containsThat { it.id == id }
    }
}
