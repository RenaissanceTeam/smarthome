package smarthome.client.fragments.scriptdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import smarthome.client.R
import smarthome.client.Condition

class ConditionFragment : Fragment() {

    private var conditions: RecyclerView? = null
    private var adapter: ConditionsAdapter? = null
    private var saveButton: FloatingActionButton? = null

    private lateinit var viewModel: ScriptDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ScriptDetailViewModel::class.java)
        } ?: throw NullPointerException("Activity is null in condition fragment")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.conditions.observe(this, Observer { adapter?.notifyDataSetChanged() })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_condition, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        setupRecyclerView()

        saveButton?.setOnClickListener { viewModel.onSaveConditionsClicked() }
    }

    private fun setupViews(view: View) {
        conditions = view.findViewById(R.id.conditions)
        saveButton = view.findViewById(R.id.save)
    }

    private fun setupRecyclerView() {
        adapter = ConditionsAdapter(viewModel)
        conditions?.layoutManager = LinearLayoutManager(context)
        conditions?.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()

        conditions = null
        adapter = null
        saveButton = null
    }
}


class ConditionsAdapter(private val viewModel: ScriptDetailViewModel) :
        RecyclerView.Adapter<ConditionViewHolder>() {

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

class ConditionViewHolder(view: View,
                          onTypeChange: (Int, String) -> Unit) : RecyclerView.ViewHolder(view) {
    private val fields = view.findViewById<LinearLayout>(R.id.fields)
    private val type = view.findViewById<RadioGroup>(R.id.type_radio_group)
    private var boundPosition: Int = -1
    private var isBinding = false

    init {
        type.setOnCheckedChangeListener { group, checkedId ->
            if (isBinding) return@setOnCheckedChangeListener

            val button = group.findViewById<RadioButton>(checkedId)
            button ?: return@setOnCheckedChangeListener

            onTypeChange(boundPosition, button.tag.toString())
        }
    }

    fun bind(condition: Condition, position: Int) {
        isBinding = true
        boundPosition = position

        selectRadioButton(condition)
        inflateFields(condition)

        isBinding = false
    }

    private fun inflateFields(condition: Condition) {
        fields.removeAllViews()
        condition.getFields().forEach { fields.addView(it.getView(fields)) }
    }

    private fun selectRadioButton(condition: Condition) {
        val shouldBeChecked = type.findViewWithTag<RadioButton>(condition.getTag())
        if (shouldBeChecked.id != type.checkedRadioButtonId) {
            type.check(shouldBeChecked.id)
        }
    }
}
