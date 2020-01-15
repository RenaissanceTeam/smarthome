package smarthome.client.presentation.devices.deviceaddition.items

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.controller_card.view.*
import smarthome.client.entity.Controller
import smarthome.client.presentation.R

class PendingController(val controller: Controller) : AbstractItem<PendingController.ViewHolder>() {
    override val layoutRes = R.layout.controller_card
    override val type = 0
    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }
    
    class ViewHolder(private val view: View): FastAdapter.ViewHolder<PendingController>(view) {
        override fun bindView(item: PendingController, payloads: MutableList<Any>) {
            view.name.text = item.controller.name.takeUnless { it.isEmpty() } ?: "Empty name"
            view.state.text = item.controller.state
            view.type.text = item.controller.type
        }
    
        override fun unbindView(item: PendingController) {
            view.name.text = null
            view.state.text = null
            view.type.text = null
        }
    }
    
}