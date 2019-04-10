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
import smarthome.client.viewpager.Pages

// todo maybe change to dagger injection of context
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = MainViewModel::class.java.simpleName

    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean>
        get() = _isAuthenticated

    private val _page = MutableLiveData<Int>()
    val page: LiveData<Int>
        get() = _page

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

    fun onBottomNavigationClick(menuItem: MenuItem) : Boolean{
        _page.value = Pages.values().find { it.menuItemId == menuItem.itemId }?.ordinal ?: return false
        return true
    }

    override fun onCleared() {
        super.onCleared()

        authDisposable.dispose()
    }
}