package smarthome.client.screens.dashboard

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
import smarthome.client.auth.Authenticator
import smarthome.library.common.IotDevice

class DashboardViewModel : ViewModel() {
    val TAG = DashboardViewModel::class.java.simpleName

    private val _devices = MutableLiveData<MutableList<IotDevice>>()
    private val _allHomeUpdateState = MutableLiveData<Boolean>()
    private val _toastMessage = MutableLiveData<String?>()

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private var devicesSubscription: Disposable? = null
    private val authSubscription: Disposable

    val devices: LiveData<MutableList<IotDevice>>
        get() = _devices

    val allHomeUpdateState: LiveData<Boolean>
        get() = _allHomeUpdateState

    val toastMessage: LiveData<String?>
        get() = _toastMessage

    init {
        authSubscription = Authenticator.isAuthenticated.subscribe { if (it) requestSmartHomeState(); }
    }

    fun requestSmartHomeState() {
        if (BuildConfig.DEBUG) Log.d(TAG, "request smart home state")

        uiScope.launch {
            _allHomeUpdateState.value = true
            if (devicesSubscription == null) tryListenForUpdates()
            try {
                _devices.value = Model.getDevices()
            } catch (e: HomeModelException) {
                if (DEBUG) Log.d(TAG, "request home state failed", e)
            }
            _allHomeUpdateState.value = false
        }
    }

    private suspend fun tryListenForUpdates() {
        try {
            devicesSubscription = Model.getDevicesObservable().subscribe {
                _devices.value = it
                _allHomeUpdateState.value = false
            }
        } catch (e: Throwable) {
            _toastMessage.value = "Can't listen for devices update"
            if (DEBUG) Log.d(TAG, "", e)
        }
    }

    fun toastShowed() { _toastMessage.value = null }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        devicesSubscription?.dispose()
    }
}