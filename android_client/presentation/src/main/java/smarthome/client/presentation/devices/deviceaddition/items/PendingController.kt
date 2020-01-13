package smarthome.client.presentation.devices.deviceaddition.items

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.controller_card.view.*
import smarthome.client.entity.Controller
import smarthome.client.presentation.R
import smarthome.client.presentation.visible
import smarthome.client.util.Data
import smarthome.client.util.DataStatus
import smarthome.client.util.ErrorStatus
import smarthome.client.util.LoadingStatus

class PendingController(val id: Long, val controller: DataStatus<Controller>) : AbstractItem<PendingController.ViewHolder>() {
    override val layoutRes = R.layout.controller_card
    override val type = 0
    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }
    
    class ViewHolder(private val view: View): FastAdapter.ViewHolder<PendingController>(view) {
        override fun bindView(item: PendingController, payloads: MutableList<Any>) {
            when (item.controller) {
                is Data -> bindData(item.controller.data)
                is LoadingStatus -> bindLoading(item.controller)
                is ErrorStatus -> bindError(item.controller)
                
            }
        }
        
        private fun bindLoading(controller: LoadingStatus<Controller>) {
            controller.lastData?.let { bindData(it.data) }
            view.progress.visible = true
        }
        
        private fun bindError(controller: ErrorStatus<Controller>) {
            controller.lastData?.let { bindData(it.data) }
            view.progress.visible = false
        }
    
        private fun bindData(controller: Controller) {
            view.name.text = controller.name.takeUnless { it.isEmpty() } ?: "Empty name"
            view.state.text = controller.state
            view.type.text = controller.type
            view.progress.visible = false
        }
    
        override fun unbindView(item: PendingController) {
            view.name.text = null
            view.state.text = null
            view.type.text = null
            view.progress.visible = false
        }
    }
}