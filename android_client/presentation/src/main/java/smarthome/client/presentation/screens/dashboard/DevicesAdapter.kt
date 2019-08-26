package smarthome.client.presentation.screens.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.presentation.R
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

class DevicesAdapter(private val inflater: LayoutInflater,
                     private val viewModel: DashboardViewModel,
                     private val clickOnDevice: (device: IotDevice?) -> Unit,
                     private val clickOnController: (controller: BaseController) -> Unit)
    : RecyclerView.Adapter<DeviceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = inflater.inflate(R.layout.device_item, parent, false)
        return DeviceViewHolder(view, clickOnDevice, clickOnController)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        viewModel.devices.value?.let { devices ->
            val device = devices[position]

            holder.bind(device)
        }
    }

    override fun getItemCount(): Int {
        return viewModel.devices.value?.size ?: 0
    }
}