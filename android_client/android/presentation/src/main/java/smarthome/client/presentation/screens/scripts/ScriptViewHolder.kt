package smarthome.client.presentation.screens.scripts

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.presentation.R
import smarthome.library.common.scripts.Script

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