package smarthome.client.presentation.scripts.addition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import smarthome.client.presentation.R
import smarthome.client.presentation.main.toolbar.ToolbarController

class AddControllersToScriptFragment : Fragment() {
    private val viewModel: AddScriptViewModel by sharedViewModel()
    private val toolbarController: ToolbarController by inject()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_controllers_to_script, container, false)
    }
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        toolbarController.setMenu(R.menu.save) {
            if (it != R.id.save) return@setMenu
            
            viewModel.onSaveClicked()
        }
        
        viewModel.finishFlow.onNavigate(this, ::finishFlow)
        
        viewModel.scriptToAdd.observe(this) {
            toolbarController.setTitle(it.name)
        }
    }
    
    private fun finishFlow() {
        findNavController().popBackStack(R.id.addScriptInfoFragment, true)
    }
}
