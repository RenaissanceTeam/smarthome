package smarthome.client.presentation.homeserver

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_homeserver.*
import smarthome.client.presentation.R
import smarthome.client.presentation.core.BaseFragment
import smarthome.client.presentation.homeserver.epoxy.HomeServerController
import smarthome.client.presentation.util.hideSoftKeyboard

class HomeServerFragment : BaseFragment() {
    private val viewModel: HomeServerViewModel by viewModels()
    private val controller = HomeServerController()

    override fun getLayout() = R.layout.fragment_homeserver

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(viewModel)

        recents_carousel.adapter = controller.adapter

        viewModel.serverUrl.observe(viewLifecycleOwner) {
            input_server.text = it
        }

        viewModel.recents.observe(viewLifecycleOwner) {
            controller.setData(it, viewModel)
        }

        viewModel.close.onNavigate(this) {
            hideSoftKeyboard()
            view.findNavController().navigateUp()
        }

        viewModel.toLogin.onNavigate(this) {
            hideSoftKeyboard()
            view.findNavController().popBackStack(R.id.loginFragment, false)
        }

        save.setOnClickListener { viewModel.save(input_server.text) }
    }
}
