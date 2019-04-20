package smarthome.client.viewpager.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.R
import smarthome.client.util.ControllerProcessor
import smarthome.library.common.BaseController

class ControllersAdapter(val controllers: MutableList<BaseController>,
                         val controllerDetailsClickListener: (controller: BaseController?) -> Unit,
                         val controllerUpdateHandler: (controller: BaseController?) -> Unit)
    : RecyclerView.Adapter<ControllersAdapter.ViewHolder>() {

    private val TAG = javaClass.name

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

    fun notifyControllerChanged(controller: BaseController) {
        notifyItemChanged(controllers.indexOf(controller))
    }

    class ViewHolder(itemView: View,
                     notifyRecycler: (controller: BaseController) -> Unit,
                     controllerDetailsClickListener: (controller: BaseController?) -> Unit,
                     controllerUpdateHandler: (controller: BaseController?) -> Unit)
        : RecyclerView.ViewHolder(itemView) {

        private val UNKNOWN_STATE = "-"

        var controller: BaseController? = null

        var controllerName: TextView = itemView.findViewById(R.id.controller_card_name)
        var controllerState: TextView = itemView.findViewById(R.id.controller_card_state)

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
                controllerName.text = controller.type ?: UNKNOWN_STATE
            else controllerName.text = controller.name

            controllerState.text = controller.state
        }
    }

}