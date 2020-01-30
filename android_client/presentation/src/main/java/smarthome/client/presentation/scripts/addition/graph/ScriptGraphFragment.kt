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
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import smarthome.client.presentation.R
import smarthome.client.presentation.main.toolbar.ToolbarController
import smarthome.client.presentation.scripts.addition.AddScriptViewModel
import smarthome.client.presentation.scripts.addition.graph.ScriptGraphViewModel

class ScriptGraphFragment : Fragment() {
    private val addScriptViewModel: AddScriptViewModel by sharedViewModel()
    private val graphViewModel: ScriptGraphViewModel by viewModels()
    private val toolbarController: ToolbarController by inject()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_script_graph, container, false)
    }
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        toolbarController.setMenu(R.menu.save) {
            if (it != R.id.save) return@setMenu
            
            addScriptViewModel.onSaveClicked()
        }
        
        addScriptViewModel.finishFlow.onNavigate(this, ::finishFlow)
        
        addScriptViewModel.scriptToAdd.observe(this) {
            toolbarController.setTitle(it.name)
        }
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
}
