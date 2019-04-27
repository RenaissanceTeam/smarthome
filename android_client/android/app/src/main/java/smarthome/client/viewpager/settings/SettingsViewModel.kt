package smarthome.client.viewpager.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import smarthome.client.auth.Authenticator
import smarthome.client.util.CloudStorages
import smarthome.library.common.message.ChangeDoNotDisturbOption

class SettingsViewModel : ViewModel() {
    val currentAccount = MutableLiveData<String>()
    val emailDisposable: Disposable
    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)


    init {
        emailDisposable = Authenticator.userEmail.subscribe { currentAccount.value = it }
    }

    override fun onCleared() {
        super.onCleared()

        emailDisposable.dispose()
        job.cancel()
    }

    fun signOut() = uiScope.launch { Authenticator.signOut() }

    fun changeDoNotDisturbMode(mode: Boolean) = uiScope.launch {
        val message = ChangeDoNotDisturbOption(Authenticator.getUserId()!!, mode)
        CloudStorages.getMessageQueue()
                .postMessage(message)
    }
}