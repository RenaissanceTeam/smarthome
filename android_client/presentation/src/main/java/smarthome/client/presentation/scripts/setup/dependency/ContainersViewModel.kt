package smarthome.client.presentation.scripts.setup.dependency

import androidx.lifecycle.MutableLiveData
import org.koin.core.KoinComponent
import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyUnit
import smarthome.client.presentation.scripts.setup.dependency.container.ContainerId
import smarthome.client.presentation.scripts.setup.dependency.container.ContainerState
import smarthome.client.presentation.util.extensions.updateWith
import smarthome.client.util.containsThat

class ContainersViewModel<T : DependencyUnit>(
    emptyUnitsCreator: (Dependency) -> List<T>
) : KoinComponent {
    val containersLiveData = MutableLiveData<List<ContainerState<T>>>()
    private val containersState = ContainersStates { emptyUnitsCreator.invoke(it) }
    
    fun setData(data: List<T>, dependency: Dependency) {
        containersLiveData.updateWith {
            containersState.apply { setData(data, dependency) }.asList()
        }
    }
    
    fun onSelect(id: ContainerId, isSelected: Boolean) {
        containersLiveData.updateWith {
            containersState.apply { select(id, isSelected) }.asList()
        }
    }
    
    fun contains(id: ContainerId): Boolean {
        return containersState.asList().containsThat { it.id == id }
    }
    
    fun getUnit(containerId: ContainerId, unitId: String): T? {
        return containersState.asList().find { it.id == containerId }?.allData?.find { it.id == unitId }
    }
    
    fun setSelectionMode(mode: Boolean) {
        containersLiveData.updateWith {
            containersState.apply { selectionMode(mode) }.asList()
        }
    }
}
