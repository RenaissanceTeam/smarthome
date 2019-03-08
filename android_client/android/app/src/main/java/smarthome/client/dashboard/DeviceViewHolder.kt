package smarthome.client.dashboard

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import smarthome.client.R
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

class DeviceViewHolder(itemView: View,
                       clickOnDevice: (device: IotDevice?) -> Unit,
                       val clickOnController: (controller: BaseController) -> Unit): RecyclerView.ViewHolder(itemView) {
    val TAG = DeviceViewHolder::class.java.simpleName

    init {
        itemView.setOnClickListener { clickOnDevice(device) }
    }

    private var device: IotDevice? = null
    private val controllers: LinearLayout = itemView.findViewById(R.id.controllers)
    private val deviceName: TextView = itemView.findViewById(R.id.device_name)
    private val deviceDescription: TextView = itemView.findViewById(R.id.device_description)

    fun bind(device: IotDevice?) {
        controllers.removeAllViews()
        this.device = device ?: return

        deviceName.text = device.name
        deviceDescription.text = device.description
        device.controllers.forEach { controllers.addView(filledControllerVH(it)) }
    }

    private fun filledControllerVH(controller: BaseController) : View {
        val view = ControllerViewHolder(LayoutInflater.from(itemView.context), controllers).filled(controller)
        view.setOnClickListener { clickOnController(controller) }
        return view
    }

}

