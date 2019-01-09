package ru.smarthome

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.smarthome.library.BaseController
import ru.smarthome.library.IotDevice
import ru.smarthome.library.SmartHome

class ControllersAdapter(private val inflater: LayoutInflater) : RecyclerView.Adapter<ControllerViewHolder>() {

    private var smartHome: SmartHome? = null

    fun loadNewHome(newHome: SmartHome?) {
        smartHome = newHome
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ControllerViewHolder {
        val view = inflater.inflate(R.layout.controller_item, parent, false)
        return ControllerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ControllerViewHolder, position: Int) {
        val device = getDeviceByControllerPosition(position)
        val controller = getControllerByPosition(position)
        holder.bind(device, controller)
    }

    override fun getItemCount(): Int {
        return smartHome?.devices?.sumBy { it.controllers.size } ?: 0
    }

    private fun getDeviceByControllerPosition(position: Int): IotDevice? {
        var skippedContrCount = 0

        for (device in smartHome!!.devices) {
            val thisContrCount = device.controllers.size
            if (position <= skippedContrCount + thisContrCount - 1) {
                return device
            }
            skippedContrCount += thisContrCount
        }
        return null
    }

    // TODO: 12/20/18 need better solution, but not now!
    private fun getControllerByPosition(position: Int): BaseController? {
        var skippedContrCount = 0
        for (device in smartHome!!.devices) {
            val thisContrCount = device.controllers.size
            if (position <= skippedContrCount + thisContrCount - 1) {
                return device.controllers[position - skippedContrCount]
            }
            skippedContrCount += thisContrCount
        }
        return null
    }
}