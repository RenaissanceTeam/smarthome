package smarthome.client.arduino.scripts.presentation.action

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.scripts.entity.action.OnOffAction
import smarthome.client.arduino.scripts.presentation.action.onoff.OnOffModelFactory
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.presentation.scripts.resolver.ActionModelResolver

class ArduinoActionModelResolver(
    private val onOffModelFactory: OnOffModelFactory
) : ActionModelResolver {
    override fun canResolve(item: Action): Boolean {
        return when (item) {
            is OnOffAction -> true
            else -> false
        }
    }
    
    override fun resolve(item: Action): EpoxyModel<*> {
        return when (item) {
            is OnOffAction -> onOffModelFactory.create(item)
            else -> throw IllegalArgumentException("can't resolve $item")
        }
    }
}