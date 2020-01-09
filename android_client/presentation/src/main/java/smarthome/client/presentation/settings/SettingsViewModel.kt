package smarthome.client.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch
import smarthome.client.domain.api.usecase.AuthenticationUseCase

class SettingsViewModel(
    private val authenticationUseCase: AuthenticationUseCase
) : ViewModel() {
    private val _currentAccount = MutableLiveData<String>()
    val currentAccount: LiveData<String>
        get() = _currentAccount
    
    private val usernameDisposable: Disposable
    
    init {
        usernameDisposable =
            authenticationUseCase.getUsername().subscribe { _currentAccount.value = it }
    }
    
    override fun onCleared() {
        super.onCleared()
        
        usernameDisposable.dispose()
    }
    
    fun signOut() = viewModelScope.launch { authenticationUseCase.signOut() }
}