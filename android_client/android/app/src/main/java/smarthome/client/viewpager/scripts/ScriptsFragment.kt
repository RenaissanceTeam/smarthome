package smarthome.client.viewpager.scripts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import smarthome.client.R
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ScriptsFragment : Fragment() {

    private val viewModel
            by lazy { ViewModelProviders.of(this).get(ScriptsViewModel::class.java) }

    private var recyclerView: RecyclerView? = null
    private var adapter: ScriptsAdapter? = null
    private var refreshLayout: SwipeRefreshLayout? = null
    private var coordinatorLayout: CoordinatorLayout? = null
    private var actionButton: FloatingActionButton? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.scripts.observe(this, Observer { adapter?.notifyDataSetChanged() })
        viewModel.refresh.observe(this, Observer { refreshLayout?.isRefreshing = it })
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scripts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindViews(view)
        setupRecyclerView()
        refreshLayout?.setOnRefreshListener {
            if (refreshLayout?.isRefreshing == true) viewModel.onRefresh()
        }
    }

    private fun setupRecyclerView() {
        val recyclerView = recyclerView ?: return
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ScriptsAdapter(viewModel)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        hideFabWhenScrolling(recyclerView)
    }

    private fun hideFabWhenScrolling(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && actionButton?.isShown == true) {
                    actionButton?.hide()
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    actionButton?.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun bindViews(root: View) {
        recyclerView = root.findViewById(R.id.scripts)
        refreshLayout = root.findViewById(R.id.refresh_layout)
        coordinatorLayout = root.findViewById(R.id.coordinator)
        actionButton = root.findViewById(R.id.add)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView = null
        adapter = null
        refreshLayout = null
        coordinatorLayout = null
        actionButton = null
    }
}

