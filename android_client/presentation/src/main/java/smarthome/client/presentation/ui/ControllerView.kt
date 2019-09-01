package smarthome.client.presentation.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import smarthome.client.presentation.R
import smarthome.library.common.BaseController

class ControllerView(root: ViewGroup) {
    private val inflater = LayoutInflater.from(root.context)
    val itemView: View = inflater.inflate(R.layout.controller_item, root, false)
    private val name: TextView = itemView.findViewById(R.id.name)
    private val state: TextView = itemView.findViewById(R.id.state)

    private var boundGuid: Long? = null


    fun bind(controller: BaseController) {
        name.text = controller.name
        state.text = controller.state.toString()
        boundGuid = controller.guid
    }

    fun onClick(listener: (Long?) -> Unit) {
        itemView.setOnClickListener { listener(boundGuid) }
    }
}

