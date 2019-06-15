package smarthome.client.presentation.screens.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.domain.usecases.AuthenticationUseCase

class SettingsViewModel : ViewModel(), KoinComponent {
    val currentAccount = MutableLiveData<String>()
    private val emailDisposable: Disposable

    private val authenticationUseCase: AuthenticationUseCase by inject()

    init {
        emailDisposable = authenticationUseCase.getEmail().subscribe { currentAccount.value = it }
    }

    override fun onCleared() {
        super.onCleared()

        emailDisposable.dispose()
    }

    fun signOut() = viewModelScope.launch { authenticationUseCase.signOut() }
}