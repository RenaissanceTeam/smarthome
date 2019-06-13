package smarthome.client

import android.app.Application
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposable
import smarthome.client.BuildConfig.DEBUG
import smarthome.client.auth.AuthUIWrapper
import smarthome.client.auth.Authenticator

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = MainViewModel::class.java.simpleName

    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean>
        get() = _isAuthenticated

    val authUiWrapper = AuthUIWrapper
    private val authDisposable: Disposable

    init {
        authDisposable = Authenticator.isAuthenticated.subscribe { _isAuthenticated.value = it}
    }

    fun onAuthSuccessful() {
        Authenticator.onNewAuth()
        if (DEBUG) Log.d(TAG, "auth successful, user id=${Authenticator.getUserId()}")
    }

    fun onAuthFailed() {
        Authenticator.onNewAuth()
        if (DEBUG) Log.d(TAG, "auth failed, user id=${Authenticator.getUserId()}")
    }

    override fun onCleared() {
        super.onCleared()

        authDisposable.dispose()
    }
}