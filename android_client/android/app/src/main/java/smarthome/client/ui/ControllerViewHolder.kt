package smarthome.client.ui

import androidx.recyclerview.widget.RecyclerView
import smarthome.client.ui.ControllerView
import smarthome.library.common.BaseController

class ControllerViewHolder(val controllerView: ControllerView) : RecyclerView.ViewHolder(controllerView.itemView) {

    fun bind(controller: BaseController) {
        controllerView.bind(controller)
    }
}