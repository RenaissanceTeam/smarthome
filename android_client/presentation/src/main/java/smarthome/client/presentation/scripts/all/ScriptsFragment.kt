package smarthome.client.presentation.scripts.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_scripts.*
import org.koin.ext.scope
import smarthome.client.entity.NOT_DEFINED_ID
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.all.epoxy.ScriptsController

class ScriptsFragment : Fragment() {
    
    private val viewModel by viewModels<ScriptsViewModel>()
    private val itemsController = ScriptsController()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scripts, container, false)
    }
    
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    
        lifecycle.addObserver(viewModel)
    
        viewModel.refresh.observe(viewLifecycleOwner) {
            refresh_layout.isRefreshing = it
        }
    
        viewModel.scripts.observe(viewLifecycleOwner) {
            itemsController.setData(it, viewModel)
        }
    
        viewModel.openNewScript.onNavigate(this, ::openAddition)
        
        script_items.layoutManager = LinearLayoutManager(context)
        script_items.adapter = itemsController.adapter
        refresh_layout.setOnRefreshListener { viewModel.onRefresh() }
        add_script.setOnClickListener { viewModel.onAddScriptClicked() }
    }
    
    
    private fun openAddition() {
        findNavController().navigate(
            ScriptsFragmentDirections.actionScriptsFragmentToAddScriptInfoFragment(scriptId = NOT_DEFINED_ID))
        
        "setup".scope.close()
    }
}