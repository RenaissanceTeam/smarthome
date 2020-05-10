package smarthome.client.presentation.auth.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_login.change_server
import kotlinx.android.synthetic.main.fragment_login.password
import kotlinx.android.synthetic.main.fragment_login.progress
import kotlinx.android.synthetic.main.fragment_login.username
import kotlinx.android.synthetic.main.fragment_signup.*
import smarthome.client.presentation.R
import smarthome.client.presentation.core.BaseFragment
import smarthome.client.presentation.util.extensions.showToast
import smarthome.client.presentation.util.hideSoftKeyboard
import smarthome.client.util.visible


class SignUpFragment : BaseFragment() {
    private val viewModel: SignUpViewModel by viewModels()

    override fun getLayout() = R.layout.fragment_signup

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(viewModel)

        viewModel.showProgress.observe(viewLifecycleOwner) { progress.visible = it }
        viewModel.errors.onToast(viewLifecycleOwner) { context?.showToast(it) }
        viewModel.openHomeServer.onNavigate(viewLifecycleOwner) { navigateToHomeServerSetup() }
        viewModel.toDashboard.onNavigate(this) { toDashboard() }
        viewModel.toLogin.onNavigate(this) { toLogin() }

        change_server.setOnClickListener { viewModel.onHomeServerClick() }
        login.setOnClickListener { viewModel.onLoginClick() }

        signup_button.setOnClickListener {
            viewModel.signup(
                    username.text,
                    password.text,
                    registration_code.text.toLongOrNull() ?: 0
            )
        }

    }

    private fun toLogin() {
        hideSoftKeyboard()
        findNavController().popBackStack(R.id.loginFragment, false)
    }

    private fun toDashboard() {
        hideSoftKeyboard()
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToDashboardFragment())
    }

    private fun navigateToHomeServerSetup() {
        hideSoftKeyboard()
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToHomeServerFragment())
    }
}

