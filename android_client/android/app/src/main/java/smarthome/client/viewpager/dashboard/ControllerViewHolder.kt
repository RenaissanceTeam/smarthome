package smarthome.client.viewpager.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import smarthome.client.R
import smarthome.library.common.BaseController

class ControllerViewHolder(inflater: LayoutInflater, root: ViewGroup) {

    private val itemView = inflater.inflate(R.layout.controller_item, root, false)
    private val guid: TextView = itemView.findViewById(R.id.controller_guid)
    private val type: TextView = itemView.findViewById(R.id.type)
    private val state: TextView = itemView.findViewById(R.id.state)
    val UNKNOWN_STATE = "-"

    fun filled(controller: BaseController): View {
        guid.text = "${controller.guid}"
        type.text = "${controller.type ?: UNKNOWN_STATE}"
        state.text = controller.state ?: UNKNOWN_STATE
        return itemView
    }
}