package ru.smarthome

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ControllersAdapter(private val inflater: LayoutInflater,
                         private val viewModel: DashboardViewModel?) : RecyclerView.Adapter<ControllerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ControllerViewHolder {
        val view = inflater.inflate(R.layout.controller_item, parent, false)
        return ControllerViewHolder(view) { controller ->  controller?.let { viewModel?.clickOnController(it)} }
    }

    override fun onBindViewHolder(holder: ControllerViewHolder, position: Int) {
        val device = viewModel?.getDevice(position)
        val controller = viewModel?.getController(position)
        holder.bind(device, controller)
    }

    override fun getItemCount(): Int {
        return viewModel?.controllersCount ?: 0
    }
}