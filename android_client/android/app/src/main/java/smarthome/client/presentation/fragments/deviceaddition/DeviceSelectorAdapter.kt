package smarthome.client.presentation.fragments.deviceaddition

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.R
import smarthome.client.presentation.ui.DialogParameters
import smarthome.client.presentation.ui.EditTextDialog

class DeviceSelectorAdapter(private val dataSource: List<Pair<String, Int>>,
                            private val onDeviceClick: (deviceSearchMethod: String, args: String?) -> Unit) : RecyclerView.Adapter<DeviceSelectorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.device_selector_item_card, parent, false)
        return ViewHolder(itemView, onDeviceClick)
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSource[position])
    }


    class ViewHolder(itemView: View,
                     onDeviceClick: (deviceSearchMethod: String, args: String?) -> Unit) : RecyclerView.ViewHolder(itemView) {

        private var method: String? = null
        private val image: ImageView = itemView.findViewById(R.id.device_selector_item_image)
        private val title: TextView = itemView.findViewById(R.id.device_selector_item_title)

        init {
            itemView.setOnClickListener {
                method?.let {
                    when (it) {
                        SEARCH_ALL_METHOD -> onDeviceClick(it, null)
                        SEARCH_GATEWAY_METHOD -> {
                            EditTextDialog.create(itemView.context,
                                    DialogParameters("Enter gateway password", false) { password ->
                                        onDeviceClick(it, password)
                                    }
                            ).show()
                        }
                    }
                }
            }
        }

        fun bind(deviceInfo: Pair<String, Int>) {
            method = deviceInfo.first
            title.text = deviceInfo.first
            image.setImageResource(deviceInfo.second)
        }
    }
}