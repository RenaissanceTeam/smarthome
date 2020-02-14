package smarthome.client.presentation.scripts.addition.dependency.container

import com.airbnb.epoxy.EpoxyModel

data class ContainerModelsHolder(
    val id: ContainerId,
    val models: List<EpoxyModel<*>>
)