package smarthome.client.viewpager.dashboard

import android.util.Log
import androidx.lifecycle.*
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import smarthome.client.BuildConfig
import smarthome.client.BuildConfig.DEBUG
import smarthome.client.HomeModelException
import smarthome.client.Model
import smarthome.client.NoDeviceException
import smarthome.library.common.BaseController
import smarthome.library.common.ControllerType
import smarthome.library.common.IotDevice

class DashboardViewModel : ViewModel() {
    val TAG = DashboardViewModel::class.java.simpleName

    private val _devices = MutableLiveData<MutableList<IotDevice>>()
    private val _allHomeUpdateState = MutableLiveData<Boolean>()

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private var disposable: Disposable? = null

    val devices: LiveData<MutableList<IotDevice>>
        get() = _devices

    val allHomeUpdateState: LiveData<Boolean>
        get() = _allHomeUpdateState

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun requestSmartHomeState() {
        if (BuildConfig.DEBUG) Log.d(TAG, "request smart home state")

        uiScope.launch {
            _allHomeUpdateState.value = true
            if (disposable == null) tryListenForUpdates()
            try {
                _devices.value = Model.getDevices()
            } catch (e: HomeModelException) {
                if (DEBUG) Log.d(TAG, "request home state failed", e)
                // todo find a way to notify user about error
            }
        }
    }

    private suspend fun tryListenForUpdates() {
        try {
            disposable = Model.getDevicesObservable().subscribe { _devices.value = it }
        } catch (e: Throwable) {
            if (DEBUG) Log.d(TAG, "can't subscribe for devices updates", e)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        disposable?.dispose()
    }

    fun receivedNewSmartHomeState() {
        if (BuildConfig.DEBUG) Log.d(TAG, "set refreshing state to false")
        _allHomeUpdateState.value = false
    }
}