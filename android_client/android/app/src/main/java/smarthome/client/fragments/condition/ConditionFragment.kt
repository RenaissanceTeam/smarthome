package smarthome.client.fragments.condition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.R
import smarthome.client.fragments.scriptdetail.ScriptDetailViewModel
import smarthome.client.Condition

class ConditionFragment : Fragment() {

    private var conditions: RecyclerView? = null
    private var adapter: ConditionsAdapter? = null
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
    }

    private fun setupViews(view: View) {
        conditions = view.findViewById(R.id.conditions)
    }

    private fun setupRecyclerView() {
        adapter = ConditionsAdapter(viewModel)
        conditions?.layoutManager = LinearLayoutManager(context)
        conditions?.adapter = adapter
    }
}


class ConditionsAdapter(private val viewModel: ScriptDetailViewModel) :
        RecyclerView.Adapter<ConditionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConditionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.condition_item, parent, false)
        return ConditionViewHolder(view)
    }

    override fun getItemCount() = viewModel.conditions.value?.count() ?: 0

    override fun onBindViewHolder(holder: ConditionViewHolder, position: Int) {
        val condition = viewModel.conditions.value?.get(position)
        condition ?: return
        holder.bind(condition)
    }
}

class ConditionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val fields = view.findViewById<LinearLayout>(R.id.fields)
    fun bind(condition: Condition) {
        fields.removeAllViews()
        condition.getFields().forEach { fields.addView(it.getView(fields)) }
    }
}
