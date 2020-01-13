package smarthome.client.presentation.devices.deviceaddition.items

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.GridLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.device_card.view.*
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.entity.Controller
import smarthome.client.presentation.R
import smarthome.client.presentation.devices.deviceaddition.AdditionViewModel
import smarthome.client.presentation.visible

open class PendingDevice(
    private val device: GeneralDeviceInfo,
    private val viewModel: AdditionViewModel
) : AbstractItem<PendingDevice.ViewHolder>() {
    private val defaultExpanded = false
    private val isExpanded = BehaviorSubject.createDefault(defaultExpanded)
    
    private val acceptVisibility = BehaviorSubject.create<Boolean>()
    private val deleteVisibility = BehaviorSubject.create<Boolean>()
    
    private val controllers = ItemAdapter<PendingController>().apply { set(device.controllers.map(::PendingController)) }
    
    override val layoutRes = R.layout.device_card
    override val type = 0
    override fun getViewHolder(v: View) = ViewHolder(v)
    
    fun onExpand() {
        val nextExpanded = (isExpanded.value ?: defaultExpanded).not()
        isExpanded.onNext(nextExpanded)
        
//        when (nextExpanded) {
//            true -> controllers.set()
//            else -> controllers.clear()
//        }
    }
    
    fun onDeviceClicked() {
        // open details
    }
    
    fun onDelete() {
    
    }
    
    fun onAccept() {
    
    }
    
    fun onControllerClicked(controller: Controller) {
        // read state
    }
    
    fun onLongControllerClicked(controller: Controller) {
        // open details
    }
    
    class ViewHolder(private val view: View) : FastAdapter.ViewHolder<PendingDevice>(view) {
        private lateinit var disposable: CompositeDisposable
        
        override fun bindView(item: PendingDevice, payloads: MutableList<Any>) {
            disposable = CompositeDisposable()
            disposable.add(item.isExpanded.subscribe {
                val rotation = when (it) {
                    true -> 180f
                    else -> 0f
                }
                
                view.expand_button.animate().rotation(rotation).interpolator =
                    AccelerateDecelerateInterpolator()
                view.controllers.visible = it
            })
            
            view.expand_button.setOnClickListener { item.onExpand() }
            itemView.setOnLongClickListener { item.onDeviceClicked(); true }
            view.delete.setOnClickListener { item.onDelete() }
            view.accept_button.setOnClickListener { item.onAccept() }
            
            view.name.text = item.device.name
            view.type.text = item.device.type
            
            view.controllers.layoutManager = GridLayoutManager(itemView.context, 2)
            view.controllers.adapter = FastAdapter.with(item.controllers).apply {
                this.onClickListener = { _, _, controllerItem, _ ->
                    item.onControllerClicked(controllerItem.controller)
                    true
                }
                this.onLongClickListener = { _, _, controllerItem, _ ->
                    item.onLongControllerClicked(controllerItem.controller)
                    true
                }
            }
        }
        
        override fun unbindView(item: PendingDevice) {
            disposable.dispose()
            view.name.text = null
            view.type.text = null
            view.expand_button.setOnClickListener(null)
            view.delete.setOnClickListener(null)
            view.accept_button.setOnClickListener(null)
            
            view.controllers.layoutManager = null
            view.controllers.adapter = null
        }
    }
}