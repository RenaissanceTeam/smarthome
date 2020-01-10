package smarthome.client.presentation.devices.deviceaddition

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_device_addition.*
import smarthome.client.entity.Controller
import smarthome.client.entity.Device
import smarthome.client.presentation.R

class AdditionFragment : Fragment(),
    ViewNotifier {
    private var adapterForDevices: DeviceAdapter? = null
    private val viewModel: AdditionViewModel by viewModels()
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        viewModel.devices.observe(this, Observer {
            adapterForDevices?.notifyDataSetChanged()
        })
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_device_addition, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        add_device_fab.setOnClickListener {
            startActivityForResult(Intent(context, AddDeviceActivity::class.java),
                DISCOVER_REQUEST_CODE)
        }
        
        add_device_recycler.layoutManager = LinearLayoutManager(view.context)
        
        resetAdapter()
        
        add_device_recycler.setHasFixedSize(true)
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == DISCOVER_REQUEST_CODE) {
            resetAdapter()
        }
    }
    
    private fun onDeviceDetailsClick(device: Device) {
        val action =
            AdditionFragmentDirections.actionAdditionFragmentToDeviceDetails(
                device.id, true)
        findNavController().navigate(action)
    }
    
    private fun onControllerDetailsClick(controller: Controller) {
        val action =
            AdditionFragmentDirections.actionAdditionFragmentToControllerDetails(
                controller.id, true)
        findNavController().navigate(action)
    }
    
    override fun onItemRemoved(pos: Int) {
        resetAdapter()
    }
    
    private fun resetAdapter() {
        adapterForDevices = createDeviceAdapter()
        add_device_recycler?.adapter = adapterForDevices
        adapterForDevices?.viewNotifier = this
    }
    
    private fun createDeviceAdapter(): DeviceAdapter {
        TODO()
    }
}