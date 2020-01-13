package smarthome.client.presentation.devices.deviceaddition.items

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.controller_card.view.*
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.domain.api.conrollers.usecases.GetControllerUseCase
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.domain.api.conrollers.usecases.ReadControllerUseCase
import smarthome.client.entity.Controller
import smarthome.client.presentation.R
import smarthome.client.presentation.visible
import smarthome.client.util.*

class PendingController(val id: Long) : AbstractItem<PendingController.ViewHolder>(), KoinComponent {
    private val observeControllerUseCase: ObserveControllerUseCase by inject()
    private val getControllerUseCase: GetControllerUseCase by inject()
    
    override val layoutRes = R.layout.controller_card
    override val type = 0
    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }
    
    suspend fun fetchController() {
        getControllerUseCase.execute(id)
    }
    
    class ViewHolder(private val view: View): FastAdapter.ViewHolder<PendingController>(view) {
        private lateinit var disposable: CompositeDisposable
        private val job = Job()
        private val uiScope = CoroutineScope(Dispatchers.Main + job)
        override fun bindView(item: PendingController, payloads: MutableList<Any>) {
            disposable = CompositeDisposable()
            disposable.add(
                item.observeControllerUseCase.execute(item.id)
                    .doOnNext { if (it is EmptyStatus) uiScope.launch { item.fetchController() }}
                    .subscribe(::bindDataStatus)
            )
        }
        
        private fun bindDataStatus(item: DataStatus<Controller>) {
            when (item) {
                is Data -> bindData(item.data)
                is LoadingStatus -> bindLoading(item)
                is ErrorStatus -> bindError(item)
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
            disposable.dispose()
            job.cancel()
            
            view.name.text = null
            view.state.text = null
            view.type.text = null
            view.progress.visible = false
        }
    }
}