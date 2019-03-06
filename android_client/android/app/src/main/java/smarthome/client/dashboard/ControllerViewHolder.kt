package smarthome.client.dashboard

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.R
import smarthome.library.common.BaseController
import smarthome.library.common.IotDevice

class ControllerViewHolder(itemView: View,
                           handler: (controller: BaseController?) -> Unit) : RecyclerView.ViewHolder(itemView) {
    val TAG = ControllerViewHolder::class.java.simpleName
    val UNKNOWN_STATE = "-"

    private val guid: TextView = itemView.findViewById(R.id.controller_guid)
    private val type: TextView = itemView.findViewById(R.id.type)
    private val state: TextView = itemView.findViewById(R.id.state)

    private var device: IotDevice? = null
    private var controller: BaseController? = null

    init {
        itemView.setOnClickListener { handler.invoke(controller) }
    }

    fun bind(device: IotDevice?, controller: BaseController?) {
        this.controller = controller
        this.device = device

        guid.text = "${controller?.guid}"
        type.text = "${controller?.type}"
        state.text = controller?.state ?: UNKNOWN_STATE
    }
}

