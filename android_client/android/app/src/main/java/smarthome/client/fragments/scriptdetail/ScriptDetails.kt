package smarthome.client.fragments.scriptdetail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_script_details.*
import kotlinx.android.synthetic.main.toolbar_with_save.*
import smarthome.client.*
import smarthome.client.R
import smarthome.client.ui.DialogParameters
import smarthome.client.ui.EditTextDialog
import smarthome.library.common.scripts.Script

class ScriptDetails: Fragment() {
    companion object {
        val FRAGMENT_TAG = "ScriptDetailsFragment"
    }

    private val viewModel: ScriptDetailViewModel by viewModels()
    private val args: ScriptDetailsArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_script_details, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onCreateScriptDetails()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.script.observe(this, ::bindViews)
        viewModel.isScriptOpen.observe(this) {
            if (!it) activity?.onBackPressed()
        }
    }

    private fun bindViews(script: Script?) {
        Log.d("ScriptDetails", "bind views for $script")
        if (script == null) {
            Log.d("ScriptDetails", "invalid script=$script, quit")
            activity?.onBackPressed()
            return
        }
        name.setText(script.name)
        setDecoratedText(condition, script.conditions.joinToString(" AND "))
        setDecoratedText(action, script.actions.joinToString(" AND "))
    }

    private fun setDecoratedText(textView: TextView, text: String) {
        if (text.isEmpty()) {
            textView.text = "Empty"
            textView.setTextColor(Color.parseColor("#888888"))
        } else {
            textView.text = text
            textView.setTextColor(Color.parseColor("#000000"))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        toolbar.setupWithNavController(findNavController(), appBarConfiguration)

        save.setOnClickListener { viewModel.onSaveScriptClicked() }

        name.setOnClickListener { EditTextDialog.create(view.context,
                DialogParameters("script name", currentValue = viewModel.script.value?.name ?: "") {
                    viewModel.scriptNameChange(it)
                }
        ).show() }

        change_condition_button.setOnClickListener {
            viewModel.onEditConditionClicked()
            openConditionDetails()
        }
        change_action_button.setOnClickListener {
            viewModel.onEditActionClicked()
            openActionDetails()
        }
        if (viewModel.script.value == null) viewModel.setScriptGuid(args.scriptGuid)
    }

    private fun openConditionDetails() {
        val action = ScriptDetailsDirections.actionScriptDetailsToConditionFragment()
        findNavController().navigate(action)
    }

    private fun openActionDetails() {
        val action = ScriptDetailsDirections.actionScriptDetailsToActionFragment()
        findNavController().navigate(action)
    }
}
