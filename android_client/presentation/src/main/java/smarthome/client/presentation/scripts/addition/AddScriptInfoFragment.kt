package smarthome.client.presentation.scripts.addition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import org.koin.android.ext.android.inject
import smarthome.client.presentation.R
import smarthome.client.presentation.main.toolbar.ToolbarController

class AddScriptInfoFragment : Fragment() {
    private val viewModel: AddScriptViewModel by viewModels()
    private val toolbarController: ToolbarController by inject()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_script, container, false)
    }
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        toolbarController.setMenu(R.menu.next) {
            if (it != R.id.next) return@setMenu
            viewModel.onNextFromScriptInfoClicked()
        }
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        
    }
}

