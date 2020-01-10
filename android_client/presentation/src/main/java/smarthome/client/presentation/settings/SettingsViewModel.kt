package smarthome.client.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.launch

class SettingsViewModel(
) : ViewModel() {
    private val _currentAccount = MutableLiveData<String>()
    val currentAccount: LiveData<String>
        get() = _currentAccount
    
    override fun onCleared() {
        super.onCleared()
    
    }
    
    fun signOut() = viewModelScope.launch {TODO() }
}