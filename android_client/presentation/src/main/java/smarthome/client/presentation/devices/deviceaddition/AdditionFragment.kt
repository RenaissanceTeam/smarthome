package smarthome.client.presentation.devices.deviceaddition

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_device_addition.*
import smarthome.client.presentation.R
import smarthome.client.presentation.devices.deviceaddition.epoxy.PendingDeviceController
import smarthome.client.presentation.visible

class AdditionFragment : Fragment() {
    val controller = PendingDeviceController()
    private val viewModel: AdditionViewModel by viewModels()
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    
        lifecycle.addObserver(viewModel)
        viewModel.deviceStates.observe(this) {
            controller.setData(it, viewModel)
        }
        viewModel.showEmpty.observe(this) {
            empty_message.visible = it
        }
        viewModel.refresh.observe(this) {
            refresh_layout.isRefreshing = it
        }
        viewModel.openDeviceDetails.onNavigate(this) {
            val view = view ?: return@onNavigate
            view.findNavController()
                .navigate(AdditionFragmentDirections.actionAdditionFragmentToDeviceDetails(it))
        }
        viewModel.openControllerDetails.onNavigate(this) {
            val view = view ?: return@onNavigate
            view.findNavController()
                .navigate(AdditionFragmentDirections.actionAdditionFragmentToControllerDetails(it))
        }
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_device_addition, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        add_device_fab.setOnClickListener {
            viewModel.onAddDeviceClicked()
        }
        
        refresh_layout.setOnRefreshListener(viewModel::onRefresh)
        
        add_device_recycler.layoutManager = LinearLayoutManager(view.context)
        add_device_recycler.adapter = controller.adapter
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == DISCOVER_REQUEST_CODE) {
            TODO()
        }
    }
}