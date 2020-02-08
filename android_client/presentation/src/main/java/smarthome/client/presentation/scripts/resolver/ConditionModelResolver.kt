package smarthome.client.presentation.scripts.resolver

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.addition.dependency.ConditionView

interface ConditionModelResolver {
    fun canResolve(condition: Condition): Boolean
    
    fun resolve(condition: Condition): EpoxyModel<ConditionView>?
}