package smarthome.client.arduino.scripts.presentation

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.scripts.entity.action.OnOffAction
import smarthome.client.arduino.scripts.entity.action.ReadAction
import smarthome.client.arduino.scripts.presentation.common.ReadActionModelFactory
import smarthome.client.arduino.scripts.presentation.onoff.OnOffModelFactory
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.presentation.scripts.resolver.ActionModelResolver

class ArduinoActionModelResolver(
    private val onOffModelFactory: OnOffModelFactory,
    private val readActionModelFactory: ReadActionModelFactory
) : ActionModelResolver {
    override fun canResolve(item: Action): Boolean {
        return when (item) {
            is OnOffAction  -> true
            is ReadAction -> true
            else -> false
        }
    }
    
    override fun resolve(item: Action): EpoxyModel<*> {
        return when (item) {
            is OnOffAction -> onOffModelFactory.create(item)
            is ReadAction -> readActionModelFactory.create(item)
            else -> throw IllegalArgumentException("can't resolve $item")
        }
    }
}