package smarthome.client.presentation.devices.deviceaddition

import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.entity.Controller
import smarthome.client.entity.Device
import smarthome.client.presentation.R

class DeviceAdapter(private val viewModel: AdditionViewModel,
                    private val deviceDetailsClickListener: (device: Device?) -> Unit,
                    private val controllerDetailsClickListener: (controller: Controller?) -> Unit)
    : RecyclerView.Adapter<DeviceAdapter.ViewHolder>(),
    ViewNotifier {
    
    
    var viewNotifier: ViewNotifier? = null
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.device_card, parent, false)
        return ViewHolder(
            itemView, false,
            deviceDetailsClickListener,
            controllerDetailsClickListener,
            { viewModel.acceptDevice(it) },
            { viewModel.rejectDevice(it) },
            ::notifyItemViewChanged,
            { viewModel.onControllerChanged(TODO()) })
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewModel.devices.value?.let { devices ->
            val device = devices[position]
            
            holder.bind(device)
        }
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) { //TODO: add animation
            viewModel.devices.value?.let { devices ->
                val device = devices[position]
                
                holder.bind(device, payloads[0] as Boolean)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }
    
    private fun notifyItemViewChanged(device: Device?, isExpanded: Boolean) {
        viewModel.devices.value?.indexOf(device)?.let {
            notifyItemChanged(it, isExpanded)
        }
    }
    
    override fun onItemRemoved(pos: Int) {
        notifyItemRemoved(pos)
        viewNotifier?.onItemRemoved(pos) // TODO: normal fix
    }
    
    class ViewHolder(itemView: View,
                     private var isExpanded: Boolean = false,
                     deviceDetailsClickListener: (device: Device?) -> Unit,
                     private val controllerDetailsClickListener: (controller: Controller?) -> Unit,
                     deviceAcceptClickListener: (device: Device?) -> Unit,
                     deviceRejectClickListener: (device: Device?) -> Unit,
                     onItemChanged: (device: Device?, isExpanded: Boolean) -> Unit,
                     private val controllerUpdateHandler: (controller: Controller?) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        
        private var device: Device? = null
        private val deviceId: TextView = itemView.findViewById(R.id.device_card_name_label)
        private val deviceType: TextView = itemView.findViewById(R.id.device_card_description_label)
        private val controllersRecycler: RecyclerView =
            itemView.findViewById(R.id.device_controllers_recycler)
        private val expandButton: ImageButton = itemView.findViewById(R.id.device_expand_button)
        private val rejectButton: CardView = itemView.findViewById(R.id.device_card_reject_button)
        private val acceptButton: CardView = itemView.findViewById(R.id.device_card_accept_button)
        
        init {
            rejectButton.visibility = VISIBLE
            acceptButton.visibility = VISIBLE
            
           
        }
    }
}