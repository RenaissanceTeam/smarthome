package smarthome.client.presentation.scripts.scriptdetail.condition

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
import kotlinx.android.synthetic.main.fragment_condition.*
import kotlinx.android.synthetic.main.toolbar_with_save.*
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.scriptdetail.ScriptDetailViewModel
import smarthome.client.presentation.ui.SwipeToDeleteCallback

class ConditionFragment : Fragment() {
    
    private var adapter: ConditionsAdapter? = null
    
    private val viewModel: ScriptDetailViewModel by viewModels()
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        viewModel.conditions.observe(this, Observer { adapter?.notifyDataSetChanged() })
        viewModel.isConditionOpen.observe(this, Observer { if (!it) activity?.onBackPressed() })
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
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
