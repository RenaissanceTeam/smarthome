package smarthome.client.fragments.scriptdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import smarthome.client.*
import smarthome.client.R
import smarthome.client.ui.DialogParameters
import smarthome.client.ui.EditTextDialog
import smarthome.client.scripts.Script

class ScriptDetails: Fragment() {
    companion object {
        val FRAGMENT_TAG = "ScriptDetailsFragment"
    }

    private lateinit var viewModel: ScriptDetailViewModel
    private var name: EditText? = null
    private var condition: TextView? = null
    private var action: TextView? = null
    private var save: ImageView? = null
    private var changeCondition: ImageView? = null
    private var changeAction: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_script_details, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ScriptDetailViewModel::class.java)
        } ?: throw NullPointerException("Activity is null in script details")
        viewModel.onCreateScriptDetails()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.script.observe(this, Observer { bindViews(it) } )
        viewModel.isScriptOpen.observe(this, Observer { if (!it) activity?.onBackPressed()} )
    }

    private fun bindViews(script: Script?) {
        Log.d("ScriptDetails", "bind views for $script")
        if (script == null) {
            Log.d("ScriptDetails", "invalid script=$script, quit")
            activity?.onBackPressed()
            return
        }
        name?.setText(script.name)
        condition?.text = script.conditions.joinToString(" AND ")
        action?.text = script.actions.toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews(view)
        name?.setOnClickListener { EditTextDialog.create(view.context,
                DialogParameters("script name", viewModel.script.value?.name ?: "") {
                    viewModel.scriptNameChange(it)
                }
        ).show() }

        changeCondition?.setOnClickListener {
            viewModel.onEditConditionClicked()
            openConditionDetails()
        }
        changeAction?.setOnClickListener { viewModel.onEditActionClicked() }
        if (viewModel.script.value == null) passGuidToViewModel()
    }

    private fun openConditionDetails() {
        (activity as? DetailsActivity)?.run { openConditionFragment() }
    }

    private fun setupViews(view: View) {
        name = view.findViewById(R.id.name)
        condition = view.findViewById(R.id.condition)
        action = view.findViewById(R.id.action)
        save = view.findViewById(R.id.save)
        changeAction = view.findViewById(R.id.change_action_button)
        changeCondition = view.findViewById(R.id.change_condition_button)

        save?.setOnClickListener { viewModel.onSaveScriptClicked() }
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
