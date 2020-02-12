package smarthome.client.presentation.scripts.addition.dependency.container.action

import com.airbnb.epoxy.EpoxyModel

data class ActionContainerState(val index: Int, val actions: List<EpoxyModel<*>>)