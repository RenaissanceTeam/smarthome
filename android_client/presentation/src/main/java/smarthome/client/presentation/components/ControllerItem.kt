package smarthome.client.presentation.components

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.controller_item.view.*
import smarthome.client.entity.Controller
import smarthome.client.presentation.R

open class ControllerItem(
    val controller: Controller?
): AbstractItem<ControllerItem.ViewHolder>() {
    override val layoutRes = R.layout.controller_item
    override val type = ComponentViewTypes.CONTROLLER.ordinal
    override fun getViewHolder(v: View) =
        ViewHolder(v)
    
    class ViewHolder(private val view: View): FastAdapter.ViewHolder<ControllerItem>(view) {
        override fun bindView(item: ControllerItem, payloads: MutableList<Any>) {
            view.controller_name.text = item.controller?.name.orEmpty()
            view.controller_type.text = item.controller?.type.orEmpty()
            view.controller_state.text = item.controller?.state.orEmpty()
        }
        
        override fun unbindView(item: ControllerItem) {
            view.controller_name.text = null
            view.controller_type.text = null
            view.controller_state.text = null
        }
    }
}