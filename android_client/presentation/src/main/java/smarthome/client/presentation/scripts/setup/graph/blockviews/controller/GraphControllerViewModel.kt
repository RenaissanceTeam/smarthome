package smarthome.client.presentation.scripts.setup.graph.blockviews.controller

import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.entity.Controller
import smarthome.client.presentation.scripts.setup.graph.blockviews.GraphBlockViewModel
import smarthome.client.util.LoadingStatus
import smarthome.client.util.data
import kotlin.properties.Delegates

class GraphControllerViewModel(
    private val viewModel: GraphBlockViewModel
) : KoinComponent {
    private val observeController: ObserveControllerUseCase by inject()
    private val disposable = CompositeDisposable()
    val data = MutableLiveData<Controller>()
    
    fun onNewBlockData(blockState: ControllerBlockState) {
        id = blockState.block.id.id
    }
    
    private var id by Delegates.observable<Long?>(null) { _, oldId, newId ->
        if (oldId != newId && newId != null) {
            observeController(newId)
        }
    }
    
    private fun observeController(id: Long) {
        disposable.clear()
        disposable.add(observeController.execute(id).subscribe { datastatus ->
            viewModel.setLoading(datastatus is LoadingStatus)
            datastatus.data?.let { data.value = it }
        })
    }
    
}