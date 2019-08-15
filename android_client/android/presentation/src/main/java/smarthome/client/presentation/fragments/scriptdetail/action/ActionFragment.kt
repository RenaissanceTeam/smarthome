package smarthome.client.presentation.fragments.scriptdetail.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_action.*
import kotlinx.android.synthetic.main.toolbar_with_save.*
import smarthome.client.presentation.R
import smarthome.client.presentation.fragments.scriptdetail.ScriptDetailViewModel
import smarthome.client.presentation.ui.SwipeToDeleteCallback

class ActionFragment : Fragment() {

    private var adapter: ActionsAdapter? = null
    private val viewModel: ScriptDetailViewModel by viewModels()

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

        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        toolbar.setupWithNavController(findNavController(), appBarConfiguration)

        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        setupRecyclerView()

        add_button.setOnClickListener { viewModel.onAddActionButtonClicked() }
        save.setOnClickListener { viewModel.onSaveActionsClicked() }
    }

    private fun setupRecyclerView() {
        adapter = ActionsAdapter(viewModel)
        actions.layoutManager = LinearLayoutManager(context)
        actions.adapter = adapter

        val context = context ?: return
        val swipeHandler = SwipeToDeleteCallback(context) { viewHolder, _ ->
            adapter?.removeAt(viewHolder.adapterPosition)
        }

        ItemTouchHelper(swipeHandler).attachToRecyclerView(actions)
    }
}
