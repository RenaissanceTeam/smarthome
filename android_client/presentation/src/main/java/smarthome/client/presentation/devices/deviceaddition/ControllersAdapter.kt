package smarthome.client.presentation.devices.deviceaddition

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.domain_api.entity.Controller
import smarthome.client.presentation.R


class ControllersAdapter(private val controllers: List<Controller>,
                         private val controllerDetailsClickListener: (controller: Controller?) -> Unit,
                         private val controllerUpdateHandler: (controller: Controller?) -> Unit)
    : RecyclerView.Adapter<ControllersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.controller_card, parent, false)
        return ViewHolder(
            itemView, ::notifyControllerChanged, controllerDetailsClickListener,
            controllerUpdateHandler)
    }

    override fun getItemCount(): Int {
        return controllers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(controllers[position])
    }

    private fun notifyControllerChanged(controller: Controller) {
        notifyItemChanged(controllers.indexOf(controller))
    }

    class ViewHolder(itemView: View,
                     notifyRecycler: (controller: Controller) -> Unit,
                     controllerDetailsClickListener: (controller: Controller?) -> Unit,
                     controllerUpdateHandler: (controller: Controller?) -> Unit)
        : RecyclerView.ViewHolder(itemView) {


        var controller: Controller? = null
        private var controllerName: TextView = itemView.findViewById(R.id.controller_card_name)
        private var controllerState: TextView = itemView.findViewById(R.id.controller_card_state)

        init {
            itemView.setOnClickListener {
//                ControllerProcessor.write(controller, controllerUpdateHandler)
                notifyRecycler(controller!!)
            }
            itemView.setOnLongClickListener {
                controllerDetailsClickListener(controller)
                true
            }
        }

        fun bind (controller: Controller?) {
            this.controller = controller ?: return

            if (controller.name.isEmpty())
                controllerName.text = "Empty name"
            else controllerName.text = controller.name

            controllerState.text = controller.state.toString()
        }
    }

}