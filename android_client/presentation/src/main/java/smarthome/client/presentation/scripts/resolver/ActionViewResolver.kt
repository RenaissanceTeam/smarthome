package smarthome.client.presentation.scripts.resolver

import android.content.Context
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.presentation.scripts.addition.dependency.action.ActionView

interface ActionViewResolver {
    fun canResolve(action: Action): Boolean
    
    fun resolve(context: Context, action: Action): ActionView?
}