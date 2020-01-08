package smarthome.client.presentation.homeserver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_homeserver.*
import smarthome.client.presentation.R

class HomeServerFragment: Fragment() {
    private val viewModel: HomeServerViewModel by viewModels()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_homeserver, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.serverUrl.observe(this) {
            input_server.setText(it)
        }
        
        viewModel.close.observe(this) {
            view.findNavController().popBackStack()
        }
        
        save.setOnClickListener { viewModel.save(input_server.text.toString()) }
    }
}

