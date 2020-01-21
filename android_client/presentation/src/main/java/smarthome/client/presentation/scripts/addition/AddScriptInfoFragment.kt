package smarthome.client.presentation.scripts.addition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_add_script.*
import org.koin.android.ext.android.inject
import smarthome.client.presentation.R
import smarthome.client.presentation.main.toolbar.ToolbarController
import smarthome.client.util.log

class AddScriptInfoFragment : Fragment() {
    private val viewModel: AddScriptViewModel by activity?.viewModels()
    private val toolbarController: ToolbarController by inject()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_script, container, false)
    }
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    
        log("view model in 1 = $viewModel")
    
        toolbarController.setMenu(R.menu.next) {
            if (it != R.id.next) return@setMenu
            viewModel.onNextFromScriptInfoClicked(
                script_name.text.toString(),
                script_description.text.toString()
            )
        }
    
        viewModel.navigateToAddingController.onNavigate(this, ::goToAddingControllers)
    }
    
    private fun goToAddingControllers() {
        findNavController().navigate(
            AddScriptInfoFragmentDirections.actionAddScriptInfoFragmentToAddControllersToScriptFragment())
    }
}

