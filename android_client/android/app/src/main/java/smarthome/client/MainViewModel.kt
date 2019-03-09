package smarthome.client

import android.app.Application
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import smarthome.client.auth.AuthUIWrapper
import smarthome.client.auth.Authenticator
import smarthome.client.viewpager.Pages

// todo maybe change to dagger injection of context
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = MainViewModel::class.java.simpleName

    private val _needAuth = MutableLiveData<Boolean>()
    val needAuth: LiveData<Boolean>
        get() = _needAuth

    private val _page = MutableLiveData<Int>()
    val page: LiveData<Int>
        get() = _page

    val authenticator = Authenticator(FirebaseAuth.getInstance())
    val authUiWrapper = AuthUIWrapper

    fun authCheck() {
        if (BuildConfig.DEBUG) Log.d(TAG, "on create in viewmodel, so auth")
        if (authenticator.isAuthenticated()) {
            Model.setupFirestore(authenticator.getUserId()!!)
            return
        }

        _needAuth.value = true
    }

    fun onAuthSuccessful() {
        if (BuildConfig.DEBUG) Log.d(TAG, "auth successful, user id=${authenticator.getUserId()}")
        Model.setupFirestore(authenticator.getUserId()!!)
    }

    fun onAuthFailed() {
        if (BuildConfig.DEBUG) Log.d(TAG, "auth failed, user id=${authenticator.getUserId()}")
    }

    fun onBottomNavigationClick(menuItem: MenuItem) : Boolean{
        _page.value = Pages.values().find { it.menuItemId == menuItem.itemId }?.ordinal ?: return false
        return true
    }
}