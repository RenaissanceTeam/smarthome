package smarthome.client.presentation.scripts.all.epoxy

import com.airbnb.epoxy.Typed2EpoxyController
import smarthome.client.presentation.components.dividerItem
import smarthome.client.presentation.components.emptyItemView
import smarthome.client.presentation.scripts.all.ScriptsViewModel
import smarthome.client.presentation.scripts.all.items.ScriptsItemState
import smarthome.client.util.forEachDivided

class ScriptsController : Typed2EpoxyController<List<ScriptsItemState>, ScriptsViewModel>() {
    override fun buildModels(scripts: List<ScriptsItemState>, viewModel: ScriptsViewModel) {

        if (scripts.isEmpty() && viewModel.notRefreshing()) {
            emptyItemView { id(0) }
        }

        scripts.forEachDivided(
                each = {
                    scriptItemView {
                        val script = it.script

                        scriptId(script.id)
                        id(script.id)
                        name(script.name)
                        description(script.description)
                        scriptEnabled(script.enabled)
                        enableInProgress(it.enableInProgress)

                        onClick { viewModel.onScriptClicked(script.id) }
                        onEnableClick { enable -> viewModel.onEnableClicked(script.id, enable) }
                    }
                },
                divide = { dividerItem { id(it) } }
        )
    }
}