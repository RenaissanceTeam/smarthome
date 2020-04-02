package smarthome.client.presentation.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_login.*
import smarthome.client.presentation.R
import smarthome.client.presentation.core.BaseFragment
import smarthome.client.presentation.util.extensions.showToast
import smarthome.client.presentation.util.hideSoftKeyboard
import smarthome.client.util.visible


class LoginFragment : BaseFragment() {
    private val viewModel: LoginViewModel by viewModels()
    
    override fun getLayout() = R.layout.fragment_login
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username.text = "a"
        password.text = "a"
        
        viewModel.showProgress.observe(viewLifecycleOwner) { progress.visible = it }
        viewModel.errors.onToast(viewLifecycleOwner) { context?.showToast(it) }
        login_button.setOnClickListener {
            viewModel.login(
                username.text,
                password.text
            )
        }
        viewModel.close.onNavigate(this) {
            hideSoftKeyboard()
            view.findNavController().popBackStack()
        }
    }
}

