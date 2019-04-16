package smarthome.client.viewpager.scripts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.R

class ScriptsAdapter(private val viewModel: ScriptsViewModel) : RecyclerView.Adapter<ScriptViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScriptViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.script_item, parent, false)
        return ScriptViewHolder(view, viewModel::onScriptClick)
    }

    override fun getItemCount() = viewModel.scripts.value?.count() ?: 0

    override fun onBindViewHolder(holder: ScriptViewHolder, position: Int) {
        val script = viewModel.scripts.value?.get(position) ?: return
        holder.bind(script)
    }
}