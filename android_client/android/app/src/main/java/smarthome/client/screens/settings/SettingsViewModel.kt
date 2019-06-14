package smarthome.client.screens.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import smarthome.client.auth.Authenticator

class SettingsViewModel : ViewModel() {
    val currentAccount = MutableLiveData<String>()
    val emailDisposable: Disposable

    init {
        emailDisposable = Authenticator.userEmail.subscribe { currentAccount.value = it }
    }

    override fun onCleared() {
        super.onCleared()

        emailDisposable.dispose()
    }

    fun signOut() = viewModelScope.launch { Authenticator.signOut() }
}