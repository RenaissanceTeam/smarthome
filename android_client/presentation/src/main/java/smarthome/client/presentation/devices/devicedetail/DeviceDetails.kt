package smarthome.client.presentation.devices.devicedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_device_details.*
import smarthome.client.entity.Device
import smarthome.client.presentation.R
import smarthome.client.presentation.devices.devicedetail.epoxy.DeviceDetailsController
import smarthome.client.presentation.ui.DialogParameters
import smarthome.client.presentation.ui.EditTextDialog
import smarthome.client.util.visible


class DeviceDetails : Fragment() {
    private val viewModel: DeviceDetailViewModel by viewModels()
    private val args: DeviceDetailsArgs by navArgs()
    private val itemsController = DeviceDetailsController()
    
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_device_details, container, false)
    }
    
    private fun bindDevice(device: Device) {
        device_name.text = device.name
        setDescription(device)
        type.text = device.type
    }
    
    private fun setDescription(device: Device) {
        val description = device.description
        if (description.isEmpty()) {
            device_description.setTextColor(resources.getColor(android.R.color.darker_gray))
            device_description.text = getString(R.string.empty_description)
        } else {
            device_description.setTextColor(resources.getColor(android.R.color.black))
            device_description.text = device.description
        }
    }
    
    private fun openControllerDetails(controllerId: Long) {
        val action = DeviceDetailsDirections.actionDeviceDetailsToControllerDetails(controllerId)
        findNavController().navigate(action)
    }
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    
        lifecycle.addObserver(viewModel)
    
        viewModel.refresh.observe(this) { progress_bar.visible = it }
        viewModel.device.observe(this, ::bindDevice)
        viewModel.controllersLiveData.observe(this) {
            itemsController.setData(it, viewModel)
        }
    
        viewModel.openController.onNavigate(this, ::openControllerDetails)
        viewModel.setDeviceId(args.deviceGuid)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        controllers.layoutManager = LinearLayoutManager(view.context)
        controllers.adapter = itemsController.adapter
    
        controllers.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    
        device_name.setOnClickListener {
            EditTextDialog.create(view.context,
                DialogParameters("device name",
                    currentValue = device_name.text?.toString().orEmpty()) {
                    viewModel.deviceNameChanged(it)
                }
            ).show()
        }
    
        device_description.setOnClickListener {
            EditTextDialog.create(view.context,
                DialogParameters("device description",
                    currentValue = device_description.text?.toString().orEmpty()) {
                    viewModel.deviceDescriptionChanged(it)
                }
            ).show()
        }
    }
}