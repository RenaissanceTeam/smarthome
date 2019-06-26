package smarthome.client.presentation.ui

import androidx.recyclerview.widget.RecyclerView
import smarthome.library.common.BaseController

class ControllerViewHolder(private val controllerView: ControllerView) : RecyclerView.ViewHolder(controllerView.itemView) {

    fun bind(controller: BaseController) {
        controllerView.bind(controller)
    }
}