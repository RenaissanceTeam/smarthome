package smarthome.client.presentation.scripts.all

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyTouchHelper
import kotlinx.android.synthetic.main.fragment_scripts.*
import smarthome.client.presentation.R
import smarthome.client.presentation.core.BaseFragment
import smarthome.client.presentation.scripts.all.epoxy.ScriptItemViewModel_
import smarthome.client.presentation.scripts.all.epoxy.ScriptsController
import smarthome.client.presentation.scripts.setup.di.setupScope
import smarthome.client.presentation.util.extensions.showToast

class ScriptsFragment : BaseFragment() {

    private val viewModel by viewModels<ScriptsViewModel>()
    private val itemsController = ScriptsController()

    override fun getLayout() = R.layout.fragment_scripts

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(viewModel)

        viewModel.refresh.observe(viewLifecycleOwner) {
            refresh_layout.isRefreshing = it
        }

        viewModel.scripts.observe(viewLifecycleOwner) {
            itemsController.setData(it, viewModel)
        }

        viewModel.errors.onToast(viewLifecycleOwner) {
            context?.showToast(it)
        }

        viewModel.openSetup.onNavigate(this, ::openSetup)

        script_items.layoutManager = LinearLayoutManager(context)
        script_items.adapter = itemsController.adapter
        refresh_layout.setOnRefreshListener { viewModel.onRefresh() }
        add_script.setOnClickListener { viewModel.onAddScriptClicked() }

        EpoxyTouchHelper.initSwiping(script_items)
                .left()
                .withTarget(ScriptItemViewModel_::class.java)
                .andCallbacks(object : EpoxyTouchHelper.SwipeCallbacks<ScriptItemViewModel_>() {
                    override fun onSwipeCompleted(
                            model: ScriptItemViewModel_,
                            itemView: View,
                            position: Int,
                            direction: Int
                    ) {
                        viewModel.onRemove(model.scriptId())
                    }
                })
    }

    private fun openSetup(id: Long) {
        findNavController().navigate(
                ScriptsFragmentDirections.actionScriptsFragmentToAddScriptInfoFragment(scriptId = id)
        )
        setupScope.close()
    }
}