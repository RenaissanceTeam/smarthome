package smarthome.client.presentation.ui

import androidx.recyclerview.widget.RecyclerView


class ControllerViewHolder(private val controllerView: ControllerView) : RecyclerView.ViewHolder(controllerView.itemView) {

    fun bind(controller: Controller) {
        controllerView.bind(controller)
    }
}