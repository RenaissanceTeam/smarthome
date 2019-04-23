package smarthome.client.fragments.scriptdetail.action

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import smarthome.client.R
import smarthome.client.fragments.scriptdetail.ScriptDetailViewModel
import smarthome.client.ui.SwipeToDeleteCallback

class ActionFragment : Fragment() {

    private var actions: RecyclerView? = null
    private var adapter: ActionsAdapter? = null
    private var addButton: FloatingActionButton? = null
    private var saveButton: ImageView? = null
    private var toolbar: Toolbar? = null

    private lateinit var viewModel: ScriptDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ScriptDetailViewModel::class.java)
        } ?: throw NullPointerException("Activity is null in action fragment")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.actions.observe(this, Observer { adapter?.notifyDataSetChanged() })
        viewModel.isActionOpen.observe(this, Observer { if (!it) activity?.onBackPressed() })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_action, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        setupRecyclerView()

        addButton?.setOnClickListener { viewModel.onAddActionButtonClicked() }
        saveButton?.setOnClickListener { viewModel.onSaveActionsClicked() }
    }

    private fun setupViews(view: View) {
        actions = view.findViewById(R.id.actions)
        addButton = view.findViewById(R.id.add_button)
        saveButton = view.findViewById(R.id.save)
        toolbar = view.findViewById(R.id.toolbar)
    }

    private fun setupRecyclerView() {
        adapter = ActionsAdapter(viewModel)
        actions?.layoutManager = LinearLayoutManager(context)
        actions?.adapter = adapter

        val context = context ?: return
        val swipeHandler = SwipeToDeleteCallback(context) { viewHolder, _ ->
            adapter?.removeAt(viewHolder.adapterPosition)
        }

        ItemTouchHelper(swipeHandler).attachToRecyclerView(actions)
    }


    override fun onDestroyView() {
        super.onDestroyView()

        actions = null
        adapter = null
        addButton = null
        saveButton = null
        toolbar = null
    }
}
