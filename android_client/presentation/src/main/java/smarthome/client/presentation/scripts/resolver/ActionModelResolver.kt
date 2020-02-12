package smarthome.client.presentation.scripts.resolver

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition

interface ActionModelResolver {
    fun canResolve(action: Action): Boolean
    
    fun resolve(action: Action): EpoxyModel<*>
}