package smarthome.client.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import androidx.lifecycle.viewModelScope
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.launch
import org.koin.core.inject
import smarthome.client.domain.api.auth.usecases.LoginUseCase
import smarthome.client.presentation.R
import smarthome.client.presentation.util.KoinViewModel
import smarthome.client.presentation.visible
import smarthome.client.util.log

class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModels()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel.showProgress.observe(this) { progress.visible = it }
        login_button.setOnClickListener {
            viewModel.login(
                username.text.toString(),
                password.text.toString()
            )
        }
    }
}

class LoginViewModel : KoinViewModel() {
    val showProgress = MutableLiveData<Boolean>(false)
    private val loginUseCase: LoginUseCase by inject()
    
    fun login(login: String, password: String) {
        viewModelScope.launch {
            showProgress.value = true
            loginUseCase.runCatching { execute(login, password) }.onFailure { log(it) }
            showProgress.value = false
        }
        
    }
}