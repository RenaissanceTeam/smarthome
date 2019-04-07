package smarthome.client.fragments.scriptdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import smarthome.client.*
import smarthome.client.R
import smarthome.client.viewpager.scripts.Script

class ScriptDetails: Fragment() {
    private val viewModel by lazy { ViewModelProviders.of(this).get(ScriptDetailViewModel::class.java) }

    companion object {
        val FRAGMENT_TAG = "ScriptDetailsFragment"
    }

    private var name: EditText? = null
    private var condition: TextView? = null
    private var action: TextView? = null
    private var save: FloatingActionButton? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_script_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.script.observe(this, Observer { bindViews(it) } )
    }

    private fun bindViews(script: Script) {
        name?.setText(script.name)
        condition?.text = script.condition.toString()
        action?.text = script.action.toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews(view)
        passGuidToViewModel()
    }

    private fun setupViews(view: View) {
        name = view.findViewById(R.id.name)
        condition = view.findViewById(R.id.condition)
        action = view.findViewById(R.id.action)
        save = view.findViewById(R.id.save)

        save?.setOnClickListener { activity?.onBackPressed() }
    }

    private fun passGuidToViewModel() {
        val guid = arguments?.getLong(SCRIPT_GUID)
        if (guid == null) {
            activity?.onBackPressed()
            return
        }
        viewModel.setScriptGuid(guid)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        name = null
        condition = null
        action = null
        save = null
    }
}
