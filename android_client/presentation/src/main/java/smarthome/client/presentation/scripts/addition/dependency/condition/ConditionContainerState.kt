package smarthome.client.presentation.scripts.addition.dependency.condition

import com.airbnb.epoxy.EpoxyModel


data class ConditionContainerState(val index: Int, val conditions: List<EpoxyModel<*>>)