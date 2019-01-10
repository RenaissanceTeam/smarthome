package ru.smarthome

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.smarthome.MainActivity.Companion.raspberryApi
import ru.smarthome.library.BaseController
import ru.smarthome.library.ControllerType
import ru.smarthome.library.IotDevice
import ru.smarthome.library.RaspberryResponse

class ControllerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    val TAG = "ControllerViewHolder"
    val UNKNOWN_STATE = "-"

    private val guid: TextView = itemView.findViewById(R.id.controller_guid)
    private val type: TextView = itemView.findViewById(R.id.type)
    private val state: TextView = itemView.findViewById(R.id.state)
    private val progressBar: ProgressBar = itemView.findViewById(R.id.progress)

    private var device: IotDevice? = null
    private var controller: BaseController? = null

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(device: IotDevice?, controller: BaseController?) {
        this.controller = controller
        this.device = device

        guid.text = "${controller?.guid}"
        type.text = "${controller?.type}"
        state.text = controller?.state ?: UNKNOWN_STATE
    }

    override fun onClick(v: View) {
        if (controller?.type == ControllerType.ARDUINO_ON_OFF) {
            if (state.text.toString() == UNKNOWN_STATE) {
                readState()
                return
            }

            when (controller?.state) {
                "0" -> changeStateTo("1")
                "1" -> changeStateTo("0")
            }
        } else {
            readState()
        }
    }

    private fun startStateChange() {
        progressBar.visibility = View.VISIBLE
        state.visibility = View.GONE
    }

    private fun endStateChange() {
        progressBar.visibility = View.GONE
        state.visibility = View.VISIBLE
    }

    private fun readState() {
        startStateChange()

        val deviceGuid = device?.guid ?: return
        val controllerGuid = controller?.guid ?: return

        raspberryApi.readControllerState(deviceGuid, controllerGuid).enqueue {
            onResponse = {
                handleResponseWithState(it)
            }

            onFailure = {
                Log.d(TAG, "onFailure: $it")
                endStateChange()
            }
        }
    }

    private fun changeStateTo(value: String) {
        startStateChange()

        val deviceGuid = device?.guid ?: return
        val controllerGuid = controller?.guid ?: return

        raspberryApi.changeControllerState(deviceGuid, controllerGuid, value).enqueue {
            onResponse = {
                handleResponseWithState(it)
            }

            onFailure = {
                Log.d(TAG, "onFailure: $it")
                endStateChange()
            }
        }
    }

    private fun handleResponseWithState(response: Response<RaspberryResponse>) {
        if (response.isSuccessful) {
            response.body()?.let {
                state.text = it.response
                controller?.state = it.response
            }
        } else {
            val message = "Returned code: ${response.code()}, body=${response.raw().body()?.string()}"
            Toast.makeText(guid.context, message, Toast.LENGTH_LONG).show()
        }

        endStateChange()
    }
}

