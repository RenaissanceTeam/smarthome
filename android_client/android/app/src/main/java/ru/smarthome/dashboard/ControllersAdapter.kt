package ru.smarthome.dashboard

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.smarthome.BuildConfig
import ru.smarthome.R

class ControllersAdapter(private val inflater: LayoutInflater,
                         private val viewModel: DashboardViewModel) : RecyclerView.Adapter<ControllerViewHolder>() {

    private val TAG = ControllersAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ControllerViewHolder {
        val view = inflater.inflate(R.layout.controller_item, parent, false)
        return ControllerViewHolder(view) { controller -> controller?.let { viewModel.clickOnController(it) } }
    }

    override fun onBindViewHolder(holder: ControllerViewHolder, position: Int) {
        if (BuildConfig.DEBUG) Log.v(TAG, "bind viewHolder $position")
        val device = viewModel.getDevice(position)
        val controller = viewModel.getController(position)

        if (BuildConfig.DEBUG) Log.v(TAG, "device= $device, controller=$controller")
        holder.bind(device, controller)
    }

    override fun getItemCount(): Int {
        if (BuildConfig.DEBUG) Log.v(TAG, "getItemCount controllersCount = ${viewModel.controllersCount}")
        return viewModel.controllersCount
    }
}