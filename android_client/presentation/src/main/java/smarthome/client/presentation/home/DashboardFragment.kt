package smarthome.client.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_dashboard.*
import smarthome.client.presentation.R
import smarthome.client.presentation.home.epoxy.DashboardController

class DashboardFragment : Fragment() {
    private val viewModel: DashboardViewModel by viewModels()
    private val itemsController = DashboardController()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        viewModel.items.observe(this) {
            itemsController.setData(it, viewModel)
        }
        
        viewModel.allHomeUpdateState.observe(this) {
            refresh_layout.isRefreshing = it
        }
    
        viewModel.openDeviceDetails.onNavigate(this, ::openDevice)
        viewModel.openControllerDetails.onNavigate(this, ::openController)
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)

        refresh_layout.setOnRefreshListener { viewModel.onRefresh() }
        
        devices.layoutManager = LinearLayoutManager(view.context)
        devices.adapter = itemsController.adapter
    }
    
    private fun openDevice(id: Long) {
        findNavController().navigate(
            DashboardFragmentDirections.actionDashboardFragmentToDeviceDetails(id))
    }
    
    private fun openController(id: Long) {
        findNavController().navigate(
            DashboardFragmentDirections.actionDashboardFragmentToControllerDetails(id))
    }
}