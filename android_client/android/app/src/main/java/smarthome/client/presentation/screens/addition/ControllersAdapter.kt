package smarthome.client.presentation.screens.addition

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.R
import smarthome.client.util.ControllerProcessor
import smarthome.library.common.BaseController

class ControllersAdapter(private val controllers: MutableList<BaseController>,
                         private val controllerDetailsClickListener: (controller: BaseController?) -> Unit,
                         private val controllerUpdateHandler: (controller: BaseController?) -> Unit)
    : RecyclerView.Adapter<ControllersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.controller_card, parent, false)
        return ViewHolder(itemView, ::notifyControllerChanged, controllerDetailsClickListener, controllerUpdateHandler)
    }

    override fun getItemCount(): Int {
        return controllers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(controllers[position])
    }

    private fun notifyControllerChanged(controller: BaseController) {
        notifyItemChanged(controllers.indexOf(controller))
    }

    class ViewHolder(itemView: View,
                     notifyRecycler: (controller: BaseController) -> Unit,
                     controllerDetailsClickListener: (controller: BaseController?) -> Unit,
                     controllerUpdateHandler: (controller: BaseController?) -> Unit)
        : RecyclerView.ViewHolder(itemView) {


        var controller: BaseController? = null
        private var controllerName: TextView = itemView.findViewById(R.id.controller_card_name)
        private var controllerState: TextView = itemView.findViewById(R.id.controller_card_state)

        init {
            itemView.setOnClickListener {
                ControllerProcessor.write(controller, controllerUpdateHandler)
                notifyRecycler(controller!!)
            }
            itemView.setOnLongClickListener {
                controllerDetailsClickListener(controller)
                true
            }
        }

        fun bind (controller: BaseController?) {
            this.controller = controller ?: return

            if (controller.name.isNullOrEmpty())
                controllerName.text = controller.type
            else controllerName.text = controller.name

            controllerState.text = controller.state
        }
    }

}