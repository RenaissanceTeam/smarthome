package smarthome.client.arduino.scripts.presentation.common

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.arduino.scripts.entity.action.ReadAction
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.presentation.scripts.setup.dependency.action.ReadActionViewModel_

class ReadActionModelFactory {
    fun create(action: ReadAction): EpoxyModel<*> {
        return ReadActionViewModel_().apply {
            id(action.id.hashCode())
        }
    }
}