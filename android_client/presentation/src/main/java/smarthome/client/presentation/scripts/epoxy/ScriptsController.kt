package smarthome.client.presentation.scripts.epoxy

import com.airbnb.epoxy.Typed2EpoxyController
import smarthome.client.presentation.components.emptyItemView
import smarthome.client.presentation.scripts.ScriptsViewModel
import smarthome.client.presentation.scripts.items.ScriptsItemState

class ScriptsController: Typed2EpoxyController<List<ScriptsItemState>, ScriptsViewModel>() {
    override fun buildModels(scripts: List<ScriptsItemState>, viewModel: ScriptsViewModel) {
        
        if (scripts.isEmpty() && viewModel.notRefreshing()) {
            emptyItemView { id(0) }
        }
        
        scripts.forEach {
            scriptItemView {
                val script = it.script
                id(script.id)
                name(script.name)
            }
        }
    }
}