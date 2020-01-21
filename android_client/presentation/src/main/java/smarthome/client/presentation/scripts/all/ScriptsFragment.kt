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
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.all.epoxy.ScriptsController

class ScriptsFragment : Fragment() {
    
    private val viewModel by viewModels<ScriptsViewModel>()
    private val itemsController = ScriptsController()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scripts, container, false)
    }
    
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        lifecycle.addObserver(viewModel)
        
        viewModel.refresh.observe(this) {
            refresh_layout.isRefreshing = it
        }
        
        viewModel.scripts.observe(this) {
            itemsController.setData(it, viewModel)
        }
    
        viewModel.openAddition.onNavigate(this, ::openAddition)
    }
    
    private fun openAddition() {
        findNavController().navigate(
            ScriptsFragmentDirections.actionScriptsFragmentToAddScriptInfoFragment())
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        script_items.layoutManager = LinearLayoutManager(context)
        script_items.adapter = itemsController.adapter
        refresh_layout.setOnRefreshListener { viewModel.onRefresh() }
        add_script.setOnClickListener { viewModel.onAddScriptClicked() }
    }
}