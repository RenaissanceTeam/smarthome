package smarthome.client.arduino.presentation.action

import android.content.Context
import smarthome.client.arduino.entity.action.OnOffAction
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.presentation.scripts.addition.dependency.action.ActionView
import smarthome.client.presentation.scripts.resolver.ActionViewResolver

class ArduinoActionViewResolver : ActionViewResolver {
    override fun canResolve(action: Action): Boolean {
        return when (action) {
            is OnOffAction -> true
            else -> false
        }
    }
    
    override fun resolve(context: Context, action: Action): ActionView? {
        return when (action) {
            is OnOffAction -> OnOffActionView(context)
            else -> null
        }
    }
}