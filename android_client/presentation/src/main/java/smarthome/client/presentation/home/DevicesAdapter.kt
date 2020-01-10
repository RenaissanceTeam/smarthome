package smarthome.client.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.entity.Controller
import smarthome.client.entity.Device
import smarthome.client.presentation.R


class DevicesAdapter(private val inflater: LayoutInflater,
                     private val viewModel: DashboardViewModel,
                     private val clickOnDevice: (device: Device?) -> Unit,
                     private val clickOnController: (controller: Controller) -> Unit)
    : RecyclerView.Adapter<DeviceViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = inflater.inflate(R.layout.device_item, parent, false)
        return DeviceViewHolder(view, clickOnDevice,
            clickOnController)
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