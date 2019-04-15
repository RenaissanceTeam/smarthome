package smarthome.client.viewpager.scripts

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.R
import smarthome.client.scripts.Script

class ScriptViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name = view.findViewById<TextView>(R.id.script_name)

    fun bind(script: Script) {
        name.text = script.name
    }
}