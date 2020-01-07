package smarthome.client.presentation.home

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.presentation.R
import smarthome.client.presentation.ui.ControllerView



class DeviceViewHolder(itemView: View,
                       clickOnDevice: (device: Device?) -> Unit,
                       val clickOnController: (controller: Controller) -> Unit): RecyclerView.ViewHolder(itemView) {

    private var device: Device? = null
    private val controllers: LinearLayout = itemView.findViewById(R.id.devices)
    private val deviceName: TextView = itemView.findViewById(R.id.device_name)
    private val deviceDescription: TextView = itemView.findViewById(R.id.device_description)

    init {
        itemView.setOnClickListener { clickOnDevice(device) }
    }

    fun bind(device: Device?) {
        controllers.removeAllViews()
        this.device = device ?: return

        deviceName.text = device.name
        deviceDescription.text = device.description
        device.controllers.forEach { controllers.addView(filledControllerVH(it)) }
    }

    private fun filledControllerVH(controller: Controller) : View {
        val controllerView = ControllerView(controllers)
        controllerView.bind(controller)

        controllerView.itemView.setOnClickListener { clickOnController(controller) }
        return controllerView.itemView
    }
}

