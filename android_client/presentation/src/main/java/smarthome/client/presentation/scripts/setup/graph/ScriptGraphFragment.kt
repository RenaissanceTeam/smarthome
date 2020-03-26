package smarthome.client.presentation.scripts.setup.graph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_script_graph.*
import org.koin.android.ext.android.inject
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.presentation.R
import smarthome.client.presentation.main.toolbar.ToolbarController
import smarthome.client.presentation.scripts.setup.SetupScriptViewModel
import smarthome.client.presentation.scripts.setup.di.setupScope

class ScriptGraphFragment : Fragment() {
    private val setupScriptViewModel by lazy { setupScope.get<SetupScriptViewModel>() }
    private val viewModel: ScriptGraphViewModel by viewModels()
    private val toolbarController: ToolbarController by inject()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_script_graph, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(viewModel)
    
        toolbarController.setMenu(R.menu.save) {
            if (it != R.id.save) return@setMenu
    
            setupScriptViewModel.onSaveClicked()
        }
        
        setupScriptViewModel.finishFlow.onNavigate(this, ::finishFlow)
        
        viewModel.title.observe(viewLifecycleOwner, toolbarController::setTitle)
    
        viewModel.setupDependency.onNavigate(this, ::setupDependency)
        
        add_controller.setOnClickListener {
            fab_menu.close(true)
            add_controllers.open()
        }
    }
    
    private fun finishFlow() {
        findNavController().popBackStack(R.id.addScriptInfoFragment, true)
    }
    
    private fun setupDependency(id: DependencyId) {
        findNavController().navigate(ScriptGraphFragmentDirections
            .actionAddControllersToScriptFragmentToSetupDependencyFragment(dependencyId = id, isNew = true))
    }
}
