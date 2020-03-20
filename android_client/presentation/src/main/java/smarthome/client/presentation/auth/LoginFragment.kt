package smarthome.client.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_login.*
import smarthome.client.presentation.R
import smarthome.client.util.visible
import smarthome.client.presentation.util.hideSoftKeyboard


class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModels()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username.text = "a"
        password.text = "a"
        
        viewModel.showProgress.observe(viewLifecycleOwner) { progress.visible = it }
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

