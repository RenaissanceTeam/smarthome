package smarthome.client.presentation.scripts

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.entity.Script
import smarthome.client.presentation.R

class ScriptViewHolder(view: View, onClick: (Script?) -> Unit) : RecyclerView.ViewHolder(view) {
    private val name = view.findViewById<TextView>(R.id.script_name)
    private var boundScript: Script? = null
    
    init {
        view.setOnClickListener { onClick(boundScript) }
    }
    
    fun bind(script: Script) {
        name.text = script.name
        boundScript = script
    }
}