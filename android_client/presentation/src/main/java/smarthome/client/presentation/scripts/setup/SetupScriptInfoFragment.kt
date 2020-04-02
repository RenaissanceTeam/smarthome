package smarthome.client.presentation.scripts.setup

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_setup_script_info.*
import org.koin.android.ext.android.inject
import smarthome.client.entity.script.Script
import smarthome.client.presentation.R
import smarthome.client.presentation.core.BackPressState
import smarthome.client.presentation.core.BaseFragment
import smarthome.client.presentation.main.toolbar.ToolbarController
import smarthome.client.presentation.scripts.setup.di.setupScope
import smarthome.client.presentation.util.confirmAction
import smarthome.client.presentation.util.hideSoftKeyboard
import smarthome.client.util.visible

class SetupScriptInfoFragment : BaseFragment() {
    private val viewModel by lazy { setupScope.get<SetupScriptViewModel>() }
    private val toolbarController: ToolbarController by inject()
    private val args: SetupScriptInfoFragmentArgs by navArgs()
    
    
    override fun getLayout() = R.layout.fragment_setup_script_info
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
            lifecycleScope.launchWhenResumed { confirmCancellation() }
        }
    }
    
    override suspend fun onBackPressed(): BackPressState {
        confirmCancellation()
        return BackPressState.CONSUMED
    }
    
    private suspend fun confirmCancellation() {
        val confirmed = confirmAction(context) {
            title = "Close without saving?"
        }
        viewModel.takeIf { confirmed }?.onCancel()
    }
    
    private fun bindScript(script: Script) {
        script_name.text = script.name
        script_description.text = script.description
    }
    
    private fun bindLoading(loading: Boolean) {
        progress.visible = loading
    }
    
    private fun goToAddingControllers() {
        hideSoftKeyboard()
        findNavController().navigate(
            SetupScriptInfoFragmentDirections.actionAddScriptInfoFragmentToAddControllersToScriptFragment())
    }
}

