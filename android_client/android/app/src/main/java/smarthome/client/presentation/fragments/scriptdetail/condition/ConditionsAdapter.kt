package smarthome.client.presentation.fragments.scriptdetail.condition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.R
import smarthome.client.presentation.fragments.scriptdetail.ScriptDetailViewModel

class ConditionsAdapter(private val viewModel: ScriptDetailViewModel) :
        RecyclerView.Adapter<ConditionViewHolder>() {

    fun removeAt(position: Int) {
        viewModel.removeConditionAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConditionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.condition_item, parent, false)
        return ConditionViewHolder(view) { position, title -> viewModel.changeConditionType(position, title) }
    }

    override fun getItemCount() = viewModel.conditions.value?.count() ?: 0

    override fun onBindViewHolder(holder: ConditionViewHolder, position: Int) {
        val condition = viewModel.conditions.value?.get(position)
        condition ?: return
        holder.bind(condition, position)
    }
}