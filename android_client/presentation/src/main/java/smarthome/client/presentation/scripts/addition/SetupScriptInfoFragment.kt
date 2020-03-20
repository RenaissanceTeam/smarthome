package smarthome.client.presentation.scripts.addition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_setup_script_info.*
import org.koin.android.ext.android.inject
import org.koin.ext.scope
import smarthome.client.entity.script.Script
import smarthome.client.presentation.R
import smarthome.client.presentation.main.toolbar.ToolbarController
import smarthome.client.presentation.util.confirmAction
import smarthome.client.util.visible

class SetupScriptInfoFragment : Fragment() {
    private val viewModel by lazy { "setup".scope.get<SetupScriptViewModel>() }
    private val toolbarController: ToolbarController by inject()
    private val args: SetupScriptInfoFragmentArgs by navArgs()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setup_script_info, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setConfirmationOnUpNavigation()
        viewModel.onScriptId(args.scriptId)
        
        viewModel.setupScript.observe(viewLifecycleOwner, ::bindScript)
        viewModel.loading.observe(viewLifecycleOwner, ::bindLoading)
        viewModel.close.onNavigate(viewLifecycleOwner) { findNavController().navigateUp() }
    
        toolbarController.setMenu(R.menu.next) {
            if (it != R.id.next) return@setMenu
            viewModel.onNextFromScriptInfoClicked(
                script_name.text,
                script_description.text
            )
        }
        
        viewModel.navigateToAddingController.onNavigate(this, ::goToAddingControllers)
    }
    
    private fun setConfirmationOnUpNavigation() {
        toolbarController.setNavigationCallback {
            lifecycleScope.launchWhenResumed {
                val confirmed = confirmAction(context) {
                    title = "Close without saving?"
                }
                viewModel.takeIf { confirmed }?.onCancel()
            }
        }
    }
    
    private fun bindScript(script: Script) {
        script_name.text = script.name
        script_description.text = script.description
    }
    
    private fun bindLoading(loading: Boolean) {
        progress.visible = loading
    }
    
    private fun goToAddingControllers() {
        findNavController().navigate(
            SetupScriptInfoFragmentDirections.actionAddScriptInfoFragmentToAddControllersToScriptFragment())
    }
}

