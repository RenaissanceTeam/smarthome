package smarthome.client.presentation.controllers.controllerdetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.conrollers.usecases.FetchControllerUseCase
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.domain.api.conrollers.usecases.UpdateControllerNameUseCase
import smarthome.client.entity.Controller
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.util.ToastLiveData
import smarthome.client.presentation.util.extensions.runInScopeLoading
import smarthome.client.util.*

class ControllerDetailViewModel : KoinViewModel() {
    val refresh = MutableLiveData<Boolean>()
    val controller = MutableLiveData<Controller>()
    val errors = ToastLiveData()
    private var controllerId: Long = 0
    private val fetchControllersUseCase: FetchControllerUseCase by inject()
    private val observeControllerUseCase: ObserveControllerUseCase by inject()
    private val updateControllerNameUseCase: UpdateControllerNameUseCase by inject()

    fun setControllerId(id: Long) {
        controllerId = id

        disposable.add(observeControllerUseCase.execute(id).subscribe {
            when (it) {
                is Data -> {
                    controller.postValue(it.data)
                    refresh.postValue(false)
                }
                is LoadingStatus -> {
                    it.data?.let(controller::postValue)
                    refresh.postValue(true)
                }
                is ErrorStatus -> {
                    it.data?.let(controller::postValue)
                    refresh.postValue(false)
                    errors.post(it.cause.message.orEmpty())
                    log(it.cause)
                }
            }
        })
    }

    override fun onResume() {
        onRefresh()
    }

    fun onRefresh() {
        viewModelScope.launch {
            fetchControllersUseCase.runCatching { execute(controllerId) }
        }
    }

    fun getCurrentControllerName(): String = controller.value?.name.orEmpty()

    fun controllerNameChanged(name: String) {
        updateControllerNameUseCase.runInScopeLoading(viewModelScope, refresh) {
            runCatching { execute(controllerId, name) }
                    .onFailure { errors.post(it.message.orEmpty()) }
        }
    }
}