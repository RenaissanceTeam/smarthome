package smarthome.client.presentation.devices.deviceaddition.items

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import smarthome.client.entity.Controller

class PendingController(private val controller: Controller) : AbstractItem<PendingController.ViewHolder>() {
    override val layoutRes: Int
        get() = TODO()
    override val type: Int
        get() = TODO()
    
    override fun getViewHolder(v: View): ViewHolder {
        TODO()
    }
    
    class ViewHolder(private val view: View): FastAdapter.ViewHolder<PendingController>(view) {
        override fun bindView(item: PendingController, payloads: MutableList<Any>) {
            TODO()
        }
    
        override fun unbindView(item: PendingController) {
            TODO()
        }
    }
    
}