package smarthome.client.viewpager.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
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

    fun signOut() = Authenticator.signOut()
}