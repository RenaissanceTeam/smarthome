package smarthome.client.presentation.devices.deviceaddition.items

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.GridLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.device_card.view.*
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.entity.Controller
import smarthome.client.presentation.R
import smarthome.client.presentation.visible

open class PendingDeviceItem(
    private val device: GeneralDeviceInfo,
    private val actions: PendingDeviceActions
) : AbstractItem<PendingDeviceItem.ViewHolder>() {
    override val layoutRes = R.layout.device_card
    override val type = 0
    override fun getViewHolder(v: View) = ViewHolder(v)
    
    class ViewHolder(private val view: View) : FastAdapter.ViewHolder<PendingDeviceItem>(view) {
        private var isExpanded = false
        
        override fun bindView(item: PendingDeviceItem, payloads: MutableList<Any>) {
            
            itemView.setOnLongClickListener {
                item.actions.onDeviceClicked()
                true
            }
            view.delete.setOnClickListener {
                it.visibility = View.INVISIBLE
                deviceRejectClickListener(device)
            }
            acceptButton.setOnClickListener {
                acceptButton.visibility = View.INVISIBLE
                rejectButton.setOnClickListener(null)
                deviceAcceptClickListener(device)
            }
            expandButton.setOnClickListener {
                isExpanded = !isExpanded
                onItemChanged(device, isExpanded)
            }
            
            
            
            
            
            view.name.text = item.device.name
            view.type.text = item.device.type
    
            if (isExpanded)
                fillControllersRecycler(item.device.controllers)
            else clearControllersRecycler()
    
            val deg = view.expand_button.rotation + 180f
            view.expand_button.animate().rotation(deg).interpolator = AccelerateDecelerateInterpolator()
        }
    
    
        private fun fillControllersRecycler(controllers: List<Controller>) {
            view.controllers.layoutManager = GridLayoutManager(itemView.context, 2)
            val items = ItemAdapter<PendingController>().apply {
                add(controllers.map(::PendingController))
            }
            view.controllers.adapter = FastAdapter.with(items)
            view.controllers.visible = true
        }
    
        private fun clearControllersRecycler() {
            view.controllers.visible = false
        }
        
        override fun unbindView(item: PendingDeviceItem) {
            view.name.text = null
            view.type.text = null
        }
    }
}