package smarthome.client.presentation.scripts.resolver

import android.content.Context
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.addition.dependency.condition.ConditionView

interface ConditionViewResolver {
    fun canResolve(condition: Condition): Boolean
    
    fun resolve(context: Context, condition: Condition): ConditionView?
}