package ru.smarthome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class DashboardFragment : Fragment() {

    private var viewModel: DashboardViewModel? = null

    private var refreshLayout: SwipeRefreshLayout? = null
    private var controllers: RecyclerView? = null
    private var adapterForControllersList: ControllersAdapter? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        viewModel?.controllers?.observe(this, Observer {
            adapterForControllersList?.notifyDataSetChanged()
            viewModel?.receivedNewSmartHomeState()
        })
        viewModel?.allHomeUpdateState?.observe(this, Observer { refreshLayout?.isRefreshing = it })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshLayout = view.findViewById(R.id.refresh_controllers)
        controllers = view.findViewById(R.id.controllers)
        controllers?.layoutManager = LinearLayoutManager(view.context)

        adapterForControllersList = ControllersAdapter(layoutInflater, viewModel)
        controllers?.adapter = adapterForControllersList

        refreshLayout?.setOnRefreshListener { viewModel?.requestSmartHomeState() }
    }
}