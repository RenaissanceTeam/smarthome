package smarthome.client.presentation.scripts.scriptdetail.action

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.scriptdetail.ScriptDetailViewModel

class ActionsAdapter(private val viewModel: ScriptDetailViewModel) :
    RecyclerView.Adapter<ActionViewHolder>() {
    
    fun removeAt(position: Int) {
        viewModel.removeActionAt(position)
        notifyItemRemoved(position)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.action_item, parent, false)
        return ActionViewHolder(view) { position, title ->
            viewModel.changeActionType(position, title)
        }
    }
    
    override fun getItemCount() = viewModel.actions.value?.count() ?: 0
    
    override fun onBindViewHolder(holder: ActionViewHolder, position: Int) {
        val action = viewModel.actions.value?.get(position)
        action ?: return
        holder.bind(action, position)
    }
}