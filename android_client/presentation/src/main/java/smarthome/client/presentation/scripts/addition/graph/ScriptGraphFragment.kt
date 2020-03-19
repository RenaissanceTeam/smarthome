package smarthome.client.presentation.scripts.addition.graph

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_script_graph.*
import kotlinx.android.synthetic.main.fragment_script_graph.view.*
import org.koin.android.ext.android.inject
import org.koin.android.scope.lifecycleScope
import org.koin.android.viewmodel.ext.android.sharedViewModel
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.presentation.R
import smarthome.client.presentation.main.toolbar.ToolbarController
import smarthome.client.presentation.scripts.addition.SetupScriptViewModel
import smarthome.client.util.log

class ScriptGraphFragment : Fragment() {
    private val setupScriptViewModel: SetupScriptViewModel by sharedViewModel()
    private val viewModel: ScriptGraphViewModel by viewModels()
    private val toolbarController: ToolbarController by inject()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_script_graph, container, false).apply {
            this.add_controllers.scope = lifecycleScope
            this.script_graph.scope = lifecycleScope
        }
    }
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        toolbarController.setMenu(R.menu.save) {
            if (it != R.id.save) return@setMenu
            
            setupScriptViewModel.onSaveClicked()
        }
        
        setupScriptViewModel.finishFlow.onNavigate(this, ::finishFlow)
        
        setupScriptViewModel.setupScript.observe(this) {
            toolbarController.setTitle(it.name)
        }
    
        viewModel.setupDependency.onNavigate(this, ::setupDependency)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
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
