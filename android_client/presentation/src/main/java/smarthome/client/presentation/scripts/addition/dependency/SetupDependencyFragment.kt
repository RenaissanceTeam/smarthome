package smarthome.client.presentation.scripts.addition.dependency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import smarthome.client.presentation.R

class SetupDependencyFragment : Fragment() {
    private val viewModel: SetupDependencyViewModel by viewModels()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.scripts_setup_dependency, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    
    }
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        lifecycle.addObserver(viewModel)
    }
}

