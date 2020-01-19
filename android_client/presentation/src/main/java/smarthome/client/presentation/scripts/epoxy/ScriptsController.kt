package smarthome.client.presentation.scripts.epoxy

import com.airbnb.epoxy.Typed2EpoxyController
import smarthome.client.presentation.scripts.ScriptsViewModel
import smarthome.client.presentation.scripts.items.ScriptsItemState

class ScriptsController: Typed2EpoxyController<List<ScriptsItemState>, ScriptsViewModel>() {
    override fun buildModels(data1: List<ScriptsItemState>?, data2: ScriptsViewModel?) {
        TODO()
    }
}