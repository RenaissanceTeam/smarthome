package smarthome.client.fragments.scriptdetail.condition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_condition.*
import kotlinx.android.synthetic.main.toolbar_with_save.*
import smarthome.client.R
import smarthome.client.fragments.scriptdetail.ScriptDetailViewModel
import smarthome.client.ui.SwipeToDeleteCallback

class ConditionFragment : Fragment() {

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
        viewModel.isConditionOpen.observe(this, Observer { if (!it) activity?.onBackPressed() })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_condition, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        toolbar.setupWithNavController(findNavController(), appBarConfiguration)

        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        setupRecyclerView()

        add_button.setOnClickListener { viewModel.onAddConditionButtonClicked() }
        save.setOnClickListener { viewModel.onSaveConditionsClicked() }
    }

    private fun setupRecyclerView() {
        adapter = ConditionsAdapter(viewModel)
        conditions.layoutManager = LinearLayoutManager(context)
        conditions.adapter = adapter

        val context = context ?: return
        val swipeHandler = SwipeToDeleteCallback(context) { viewHolder, _ ->
            adapter?.removeAt(viewHolder.adapterPosition)
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(conditions)
    }

}
