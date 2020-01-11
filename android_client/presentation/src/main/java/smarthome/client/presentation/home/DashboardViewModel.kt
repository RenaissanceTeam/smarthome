package smarthome.client.presentation.home

import androidx.lifecycle.*
import com.mikepenz.fastadapter.GenericItem
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.devices.usecase.GetGeneralDevicesInfo
import smarthome.client.presentation.util.KoinViewModel


class DashboardViewModel : KoinViewModel(), LifecycleObserver {
    private val getGeneralDevicesInfoUseCase: GetGeneralDevicesInfo by inject()
    
    val items = MutableLiveData<List<GenericItem>>()
    val allHomeUpdateState = MutableLiveData<Boolean>()
    
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        onRefresh()
    }
    fun onRefresh() {
        viewModelScope.launch {
            allHomeUpdateState.value = true
    
            getGeneralDevicesInfoUseCase.runCatching { execute() }.onSuccess {
                val items = it.map { device -> DeviceItem(device.name, device.type) }
                this@DashboardViewModel.items.postValue(items)
            }
    
            allHomeUpdateState.value = false
        }
    }
}