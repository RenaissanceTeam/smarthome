package smarthome.client.presentation.devices.devicedetail

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.controller_item.view.*
import smarthome.client.entity.Controller
import smarthome.client.presentation.R

class ControllerItem(
    val controller: Controller
) : AbstractItem<ControllerItem.ViewHolder>() {
    override val layoutRes = R.layout.controller_item
    override val type = ControllerItems.CONTROLLER.ordinal
    override fun getViewHolder(v: View) = ViewHolder(v)
    
    class ViewHolder(private val view: View) : FastAdapter.ViewHolder<ControllerItem>(view) {
        override fun bindView(item: ControllerItem, payloads: MutableList<Any>) {
            view.name.text = item.controller.name
            view.type.text = item.controller.type
            view.state.text = item.controller.state
        }
        
        override fun unbindView(item: ControllerItem) {
            view.name.text = null
            view.type.text = null
            view.state.text = null
        }
    }
}