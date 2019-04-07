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
    private val name: TextView = itemView.findViewById(R.id.name)
    private val state: TextView = itemView.findViewById(R.id.state)
    private val UNKNOWN_STATE = "-"

    private var boundGuid: Long? = null


    fun bind(controller: BaseController) {
        if (controller.name.isNullOrEmpty()) {
            name.text = "${controller.type ?: UNKNOWN_STATE}"
        } else {
            name.text = controller.name
        }

        state.text = controller.state ?: UNKNOWN_STATE

        boundGuid = controller.guid
    }

    fun onClick(listener: (Long?) -> Unit) {
        itemView.setOnClickListener { listener(boundGuid) }
    }
}