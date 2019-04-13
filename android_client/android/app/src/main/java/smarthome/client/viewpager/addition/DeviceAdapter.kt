package smarthome.client.viewpager.addition

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.BuildConfig.DEBUG
import smarthome.client.R
import smarthome.library.common.IotDevice

class DeviceAdapter(private val viewModel: AdditionViewModel,
                    val deviceAcceptClickListener: (device: IotDevice?) -> Unit,
                    val deviceRejectClickListener: (device: IotDevice?) -> Unit) : RecyclerView.Adapter<DeviceAdapter.ViewHolder>() {

    private val TAG = javaClass.name

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.device_card, parent, false)
        return ViewHolder(itemView, deviceAcceptClickListener, deviceRejectClickListener)
    }

    override fun getItemCount(): Int {
        return viewModel.devices.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewModel.devices.value?.let { devices ->
            val device = devices.get(position)

            if (DEBUG) Log.v(TAG, "device= $device")
            holder.bind(device)
        }
    }


    class ViewHolder(itemView: View,
                     deviceAcceptClickListener: (device: IotDevice?) -> Unit,
                     deviceRejectClickListener: (device: IotDevice?) -> Unit) : RecyclerView.ViewHolder(itemView) {

        private var device: IotDevice? = null
        val deviceName: TextView = itemView.findViewById(R.id.device_card_name_label)
        val deviceDescription: TextView = itemView.findViewById(R.id.device_card_description_label)
        val controllersRecycler: RecyclerView = itemView.findViewById(R.id.device_controllers_recycler)
        val rejectButton: CardView = itemView.findViewById(R.id.device_card_reject_button)
        val acceptButton: CardView = itemView.findViewById(R.id.device_card_accept_button)

        init {
            rejectButton.setOnClickListener {
                rejectButton.visibility = INVISIBLE
                deviceRejectClickListener(device)
            }
            acceptButton.setOnClickListener {
                acceptButton.visibility = INVISIBLE
                deviceAcceptClickListener(device)
            }
        }

        fun bind(device: IotDevice?) {
            this.device = device ?: return

            deviceName.text = device.name
            deviceDescription.text = device.description
        }
    }
}