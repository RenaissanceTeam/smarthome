package smarthome.client.fragments.devicedetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.ui.ControllerView
import smarthome.client.ui.ControllerViewHolder

class ControllersAdapter(private val viewModel: DeviceDetailViewModel) : RecyclerView.Adapter<ControllerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ControllerViewHolder {
        val view = ControllerView(parent)
        view.onClick { it?.let { viewModel.onControllerClick(it) } }
        return ControllerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ControllerViewHolder, position: Int) {
        val controller = viewModel.device.value?.controllers?.get(position) ?: return
        holder.bind(controller)
    }

    override fun getItemCount(): Int {
        return viewModel.device.value?.controllers?.size ?: 0
    }
}
