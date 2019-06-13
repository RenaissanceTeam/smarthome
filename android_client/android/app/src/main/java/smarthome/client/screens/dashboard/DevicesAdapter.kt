package smarthome.client.screens.dashboard

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.BuildConfig
import smarthome.client.R
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

class DevicesAdapter(private val inflater: LayoutInflater,
                     private val viewModel: DashboardViewModel,
                     val clickOnDevice: (device: IotDevice?) -> Unit,
                     val clickOnController: (controller: BaseController) -> Unit) : RecyclerView.Adapter<DeviceViewHolder>() {

    private val TAG = DevicesAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = inflater.inflate(R.layout.device_item, parent, false)
        return DeviceViewHolder(view, clickOnDevice, clickOnController)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        if (BuildConfig.DEBUG) Log.v(TAG, "bind viewHolder $position")
        viewModel.devices.value?.let { devices ->
            val device = devices.get(position)

            if (BuildConfig.DEBUG) Log.v(TAG, "device= $device")
            holder.bind(device)
        }
    }

    override fun getItemCount(): Int {
        if (BuildConfig.DEBUG) Log.v(TAG, "getItemCount devicesCount = ${viewModel.devices.value?.size}")
        return viewModel.devices.value?.size ?: 0
    }
}