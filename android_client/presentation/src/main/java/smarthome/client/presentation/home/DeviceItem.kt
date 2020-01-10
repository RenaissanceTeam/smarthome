package smarthome.client.presentation.home

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import kotlinx.android.synthetic.main.device_item.view.*
import smarthome.client.presentation.R

open class DeviceItem(
    private val name: String,
    private val deviceType: String
): AbstractItem<DeviceItem.ViewHolder>() {
    override val layoutRes = R.layout.device_item
    override val type = DashboardViewTypes.DEVICE.ordinal
    override fun getViewHolder(v: View) = ViewHolder(v)
    
    class ViewHolder(private val view: View): FastAdapter.ViewHolder<DeviceItem>(view) {
        override fun bindView(item: DeviceItem, payloads: MutableList<Any>) {
            view.device_name.text = item.name
            view.device_type.text = item.deviceType
        }
    
        override fun unbindView(item: DeviceItem) {
            view.device_name.text = null
            view.device_type.text = null
        }
    }
}