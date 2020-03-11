package smarthome.client.arduino.presentation.action

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.entity.action.OnOffActionData
import smarthome.client.arduino.presentation.action.onoff.OnOffModelFactory
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.presentation.scripts.resolver.ActionModelResolver

class ArduinoActionModelResolver(
    private val onOffModelFactory: OnOffModelFactory
) : ActionModelResolver {
    override fun canResolve(item: Action): Boolean {
        return when (item.data) {
            is OnOffActionData -> true
            else -> false
        }
    }
    
    override fun resolve(item: Action): EpoxyModel<*> {
        return when (item.data) {
            is OnOffActionData -> onOffModelFactory.create(item)
            else -> throw IllegalArgumentException("can't resolve $item")
        }
    }
}