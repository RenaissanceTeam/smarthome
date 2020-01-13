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
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import kotlinx.android.synthetic.main.fragment_device_details.*
import smarthome.client.entity.Device
import smarthome.client.presentation.R
import smarthome.client.presentation.components.ControllerItem
import smarthome.client.presentation.ui.DialogParameters
import smarthome.client.presentation.ui.EditTextDialog
import smarthome.client.presentation.visible


class DeviceDetails : Fragment() {
    private val viewModel: DeviceDetailViewModel by viewModels()
    private val args: DeviceDetailsArgs by navArgs()
    private val itemAdapter = GenericItemAdapter()
    
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
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(viewModel)
    
        viewModel.refresh.observe(this) { progress_bar.visible = it }
        viewModel.device.observe(this, ::bindDevice)
        viewModel.controllers.observe(this) {
            itemAdapter.set(it)
        }
        viewModel.openController.onNavigate(this, ::openControllerDetails)
    
        controllers.layoutManager = LinearLayoutManager(view.context)
        controllers.adapter = FastAdapter.with(itemAdapter).apply {
            onClickListener = { _, _, item, _ ->
                when (item) {
                    is ControllerItem -> viewModel.onControllerClick(item.controller.id)
                }
                true
            }
        }
    
        controllers.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    
        viewModel.setDeviceId(args.deviceGuid)
    
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