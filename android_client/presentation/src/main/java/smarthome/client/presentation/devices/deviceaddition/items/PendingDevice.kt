package smarthome.client.presentation.devices.deviceaddition.items

import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.GridLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.items.AbstractItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.device_card.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.presentation.R
import smarthome.client.presentation.devices.deviceaddition.AdditionViewModel
import smarthome.client.presentation.replace
import smarthome.client.presentation.visible
import smarthome.client.util.Data
import smarthome.client.util.ErrorStatus
import smarthome.client.util.LoadingStatus

open class PendingDevice(
    val device: GeneralDeviceInfo,
    private val viewModel: AdditionViewModel
) : AbstractItem<PendingDevice.ViewHolder>(), KoinComponent {
    private val defaultExpanded = false
    private val isExpanded = BehaviorSubject.createDefault(defaultExpanded)
    private val uiScope = CoroutineScope(Dispatchers.Main)
    
    private val acceptInProgress = BehaviorSubject.createDefault(false)
    private val deleteInProgress = BehaviorSubject.createDefault(false)
    
    private val controllers = ItemAdapter<PendingController>()
        .apply { set(device.controllers.map { PendingController(it.id) }) }
    
    override val layoutRes = R.layout.device_card
    override val type = 0
    override fun getViewHolder(v: View) = ViewHolder(v)
    
    fun onExpand() {
        val nextExpanded = (isExpanded.value ?: defaultExpanded).not()
        isExpanded.onNext(nextExpanded)
    }
    
    fun onDeviceClicked() {
        viewModel.onDeviceClicked(device.id)
    }
    
    fun onDelete() {
        uiScope.launch {
            deleteInProgress.onNext(true)
            viewModel.runCatching { declineDevice(device.id) }
            deleteInProgress.onNext(false)
        }
    }
    
    fun onAccept() {
        uiScope.launch {
            acceptInProgress.onNext(true)
            viewModel.runCatching { acceptDevice(device.id) }
            acceptInProgress.onNext(false)
        }
    }
    
    fun onControllerClicked(id: Long) {
        viewModel.onControllerClicked(id)
    }
    
    fun onLongControllerClicked(id: Long) {
        viewModel.onControllerLongClicked(id)
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
            setDeleteAction(item)
            setAcceptAction(item)
            
            view.name.text = item.device.name
            view.type.text = item.device.type
            
            view.controllers.layoutManager = GridLayoutManager(itemView.context, 2)
            view.controllers.adapter = FastAdapter.with(item.controllers).apply {
                this.onClickListener = { _, _, controllerItem, _ ->
                    item.onControllerClicked(controllerItem.id)
                    true
                }
                this.onLongClickListener = { _, _, controllerItem, _ ->
                    item.onLongControllerClicked(controllerItem.id)
                    true
                }
            }
            disposable.add(item.acceptInProgress.subscribe {
                when (it) {
                    true -> {
                        view.accept_button.visibility = View.INVISIBLE
                        clearDeleteAction()
                    }
                    false -> {
                        view.accept_button.visibility = View.VISIBLE
                        setDeleteAction(item)
                    }
                }
            })
            
              disposable.add(item.deleteInProgress.subscribe {
                when (it) {
                    true -> {
                        view.delete.visibility = View.INVISIBLE
                        clearAcceptAction()
                    }
                    false -> {
                        view.delete.visibility = View.VISIBLE
                        setAcceptAction(item)
                    }
                }
            })
        }
    
        private fun setAcceptAction(item: PendingDevice) {
            view.accept_button.setOnClickListener { item.onAccept() }
        }
    
        private fun setDeleteAction(item: PendingDevice) {
            view.delete.setOnClickListener { item.onDelete() }
        }
    
        private fun clearDeleteAction() {
            view.delete.setOnClickListener(null)
        }
    
        private fun clearAcceptAction() {
            view.accept_button.setOnClickListener(null)
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