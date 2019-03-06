package smarthome.client

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import smarthome.client.auth.AuthUIWrapper
import smarthome.client.auth.Authenticator

// todo maybe change to dagger injection of context
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = MainViewModel::class.java.simpleName

    private val _needAuth = MutableLiveData<Boolean>()
    val needAuth: LiveData<Boolean>
        get() = _needAuth

    val authenticator = Authenticator(FirebaseAuth.getInstance())
    val authUiWrapper = AuthUIWrapper

    fun authCheck() {
        if (BuildConfig.DEBUG) Log.d(TAG, "on create in viewmodel, so auth")
        if (true || authenticator.isAuthenticated()) return

        _needAuth.value = true
    }

    fun onAuthSuccessful() {
        if (BuildConfig.DEBUG) Log.d(TAG, "auth successful, user id=${authenticator.getUserId()}")
    }

    fun onAuthFailed() {
        if (BuildConfig.DEBUG) Log.d(TAG, "auth failed, user id=${authenticator.getUserId()}")
    }
}