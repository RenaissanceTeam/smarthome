package smarthome.client.presentation.homeserver.epoxy

import com.airbnb.epoxy.Typed2EpoxyController
import smarthome.client.entity.HomeServer
import smarthome.client.presentation.components.dividerItem
import smarthome.client.presentation.components.emptyItemView
import smarthome.client.presentation.homeserver.HomeServerViewModel
import smarthome.client.util.forEachDivided

class HomeServerController : Typed2EpoxyController<List<HomeServer>, HomeServerViewModel>() {

    override fun buildModels(data: List<HomeServer>, viewModel: HomeServerViewModel) {

        if (data.isEmpty()) emptyItemView { id(0) }

        data.forEachDivided(
                each = { server ->
                    homeServerItem {
                        id(server.id)
                        url(server.url)
                    }
                },
                divide = { dividerItem { id(it) } }
        )
    }
}