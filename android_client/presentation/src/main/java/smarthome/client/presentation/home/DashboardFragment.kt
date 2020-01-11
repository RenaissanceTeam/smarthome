package smarthome.client.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.android.synthetic.main.fragment_dashboard.*
import smarthome.client.entity.Controller
import smarthome.client.entity.Device
import smarthome.client.presentation.R


class DashboardFragment : Fragment() {
    private val viewModel: DashboardViewModel by viewModels()
    private val itemsAdapter = GenericItemAdapter()
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        viewModel.items.observe(this) {
            itemsAdapter.set(it)
        }
        
        viewModel.allHomeUpdateState.observe(this) {
            refresh_layout.isRefreshing = it
        }
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
        devices.adapter = FastAdapter.with(itemsAdapter)
        
        devices.addItemDecoration(DividerItemDecoration(context, VERTICAL))
    }
    
    private fun onDeviceClick(device: Device?) {
        device ?: return
        
        val action =
            DashboardFragmentDirections.actionDashboardFragmentToDeviceDetails(
                device.id)
        findNavController().navigate(action)
    }
    
    private fun onControllerClick(controller: Controller) {
        val action =
            DashboardFragmentDirections.actionDashboardFragmentToControllerDetails(
                controller.id)
        findNavController().navigate(action)
    }
}