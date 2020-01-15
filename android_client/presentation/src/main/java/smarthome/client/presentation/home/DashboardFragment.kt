package smarthome.client.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import kotlinx.android.synthetic.main.fragment_dashboard.*
import smarthome.client.presentation.R
import smarthome.client.presentation.components.ControllerItem
import smarthome.client.presentation.components.DeviceItem

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
        devices.adapter = FastAdapter.with(itemsAdapter).apply {
            onClickListener = { view, adapter, item, position ->
                when (item) {
                    is ControllerItem -> onControllerClick(item.controller.id)
                    is DeviceItem -> onDeviceClick(item.device.id)
                    else -> false
                }
            }
        }
        
        devices.addItemDecoration(DividerItemDecoration(context, VERTICAL))
    }
    
    private fun onDeviceClick(deviceId: Long): Boolean {
        val action = DashboardFragmentDirections.actionDashboardFragmentToDeviceDetails(deviceId)
        findNavController().navigate(action)
        return true
    }
    
    private fun onControllerClick(controllerId: Long): Boolean {
        val action =
            DashboardFragmentDirections.actionDashboardFragmentToControllerDetails(controllerId)
        findNavController().navigate(action)
        return true
    }
}