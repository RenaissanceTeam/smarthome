package smarthome.client.presentation.homeserver

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_homeserver.*
import smarthome.client.presentation.R
import smarthome.client.presentation.core.BaseFragment
import smarthome.client.presentation.util.hideSoftKeyboard

class HomeServerFragment : BaseFragment() {
    private val viewModel: HomeServerViewModel by viewModels()
    
    override fun getLayout() = R.layout.fragment_homeserver
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(viewModel)
        
        viewModel.serverUrl.observe(viewLifecycleOwner) {
            input_server.setText(it)
        }
        
        viewModel.close.onNavigate(this) {
            hideSoftKeyboard()
            view.findNavController().popBackStack()
        }
        
        save.setOnClickListener { viewModel.save(input_server.text.toString()) }
    }
}
