package smarthome.client.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import smarthome.client.R
import smarthome.library.common.BaseController

class ControllerView(root: ViewGroup) {
    private val inflater = LayoutInflater.from(root.context)
    val itemView = inflater.inflate(R.layout.controller_item, root, false)
    private val guid: TextView = itemView.findViewById(R.id.controller_guid)
    private val type: TextView = itemView.findViewById(R.id.type)
    private val state: TextView = itemView.findViewById(R.id.state)

    val UNKNOWN_STATE = "-"

    fun bind(controller: BaseController) {
        guid.text = "${controller.guid}"
        type.text = "${controller.type ?: UNKNOWN_STATE}"
        state.text = controller.state ?: UNKNOWN_STATE
    }
}