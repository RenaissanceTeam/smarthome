package smarthome.client.presentation.devices.deviceaddition

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import kotlinx.android.synthetic.main.fragment_device_addition.*
import smarthome.client.presentation.R
import smarthome.client.presentation.components.ControllerItem
import smarthome.client.presentation.components.DeviceItem

class AdditionFragment : Fragment() {
    private var itemsAdapter = GenericItemAdapter()
    private val viewModel: AdditionViewModel by viewModels()
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    
        lifecycle.addObserver(viewModel)
        viewModel.devices.observe(this) {
            TODO()
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
    
        add_device_recycler.adapter = FastAdapter.with(itemsAdapter).also {
            it.onClickListener = { _, _, item, _ ->
                when (item) {
                    is DeviceItem -> viewModel.onDeviceClicked(item.device.id)
                    is ControllerItem -> viewModel.onControllerClicked(item.controller.id)
                    else -> {
                    }
                }
                true
            }
        }
        add_device_recycler.layoutManager = LinearLayoutManager(view.context)
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == DISCOVER_REQUEST_CODE) {
            TODO()
        }
    }
    
    
    // navigation
    
    //            startActivityForResult(Intent(context, AddDeviceActivity::class.java), DISCOVER_REQUEST_CODE)
//
//    private fun onDeviceDetailsClick(device: Device) {
//        val action =
//            AdditionFragmentDirections.actionAdditionFragmentToDeviceDetails(
//                device.id, true)
//        findNavController().navigate(action)
//    }
//
//    private fun onControllerDetailsClick(controller: Controller) {
//        val action =
//            AdditionFragmentDirections.actionAdditionFragmentToControllerDetails(
//                controller.id, true)
//        findNavController().navigate(action)
//    }
}