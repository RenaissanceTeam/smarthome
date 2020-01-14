package smarthome.client.presentation.devices.deviceaddition.epoxy

import com.airbnb.epoxy.Typed2EpoxyController
import com.airbnb.epoxy.TypedEpoxyController
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo

class PendingDeviceController : TypedEpoxyController<List<GeneralDeviceInfo>>() {
    private val expandedStates = mutableMapOf<Long, Boolean>()
    private val defaultExpanded = false
    
    override fun buildModels(data: List<GeneralDeviceInfo>) {
        data.forEach {
            pendingDeviceView {
                device(it)
                expanded(expandedStates[it.id] ?: defaultExpanded)
            }
        }
    }
}
