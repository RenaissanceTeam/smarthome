package smarthome.client.presentation.scripts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_scripts.*
import smarthome.client.domain.api.entity.Script
import smarthome.client.presentation.NEW_SCRIPT_GUID
import smarthome.client.presentation.R


class ScriptsFragment : Fragment() {

    private val viewModel: ScriptsViewModel by viewModels()

    private var adapter: ScriptsAdapter? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.scripts.observe(this) { adapter?.notifyDataSetChanged() }
        viewModel.refresh.observe(this) { refresh_layout.isRefreshing = it }
        viewModel.openScriptDetails.observe(this, Observer {
            it ?: return@Observer
//            openScriptDetails(it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scripts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        refresh_layout.setOnRefreshListener {
            if (refresh_layout.isRefreshing) viewModel.onRefresh()
        }
        add.setOnClickListener { openScriptDetails(null) }
    }

    private fun openScriptDetails(script: Script?) {
//        val action =
//            ScriptsFragmentDirections.actionScriptsFragmentToScriptDetails(
//                script?.guid ?: NEW_SCRIPT_GUID)
//        findNavController().navigate(action)
        
        TODO()
    }

    private fun setupRecyclerView() {
        scripts.layoutManager = LinearLayoutManager(context)
        adapter = ScriptsAdapter(viewModel)
        scripts.adapter = adapter
        scripts.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        hideFabWhenScrolling(scripts)
    }

    private fun hideFabWhenScrolling(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && add.isShown) {
                    add.hide()
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    add.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }
}

