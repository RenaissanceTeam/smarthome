package smarthome.client.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.usecase.DevicesUseCase
import smarthome.client.entity.Device
import smarthome.client.presentation.util.KoinViewModel


class DashboardViewModel : KoinViewModel() {
    private val devicesUseCase: DevicesUseCase by inject()
    private val _devices = MutableLiveData<MutableList<Device>>()
    private val _allHomeUpdateState = MutableLiveData<Boolean>()
    private val _toastMessage = MutableLiveData<String?>()
    
    private var devicesSubscription: Disposable? = null
    
    val devices: LiveData<MutableList<Device>>
        get() = _devices
    
    val allHomeUpdateState: LiveData<Boolean>
        get() = _allHomeUpdateState
    
    val toastMessage: LiveData<String?>
        get() = _toastMessage
    
    
    fun onRefresh() {
        viewModelScope.launch {
            _allHomeUpdateState.value = true
            
            devicesSubscription = devicesUseCase.getDevices()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { _toastMessage.value = "Error $it" }
                .subscribe {
                    _devices.value = it
                    _allHomeUpdateState.value = false
                }
        }
    }
    
    fun toastShowed() {
        _toastMessage.value = null
    }
    
    override fun onCleared() {
        super.onCleared()
        devicesSubscription?.dispose()
    }
}