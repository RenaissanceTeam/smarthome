package smarthome.client.presentation.scripts.setup.dependency

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.scripts_setup_dependency.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.ACTION_CONTAINER_CONTROLLER
import smarthome.client.presentation.CONDITION_CONTAINER_CONTROLLER
import smarthome.client.presentation.R
import smarthome.client.presentation.core.BackPressState
import smarthome.client.presentation.core.BaseFragment
import smarthome.client.presentation.main.toolbar.ToolbarController
import smarthome.client.presentation.scripts.setup.SetupScriptViewModel
import smarthome.client.presentation.scripts.setup.dependency.container.ContainerState
import smarthome.client.presentation.scripts.setup.dependency.container.ContainersController
import smarthome.client.presentation.scripts.setup.di.setupScope
import smarthome.client.presentation.util.confirmAction
import smarthome.client.util.visible

class SetupDependencyFragment : BaseFragment() {
    private val viewModel: SetupDependencyViewModel by viewModels()
    private val navArgs: SetupDependencyFragmentArgs by navArgs()
    private val toolbarController: ToolbarController by inject()
    private val setupScriptViewModel by lazy { setupScope.get<SetupScriptViewModel>() }
    private val conditionsController: ContainersController<Condition>
        by inject(named(CONDITION_CONTAINER_CONTROLLER)) { parametersOf(viewModel::onConditionScrolled) }
    private val actionsController: ContainersController<Action>
        by inject(named(ACTION_CONTAINER_CONTROLLER)) { parametersOf(viewModel::onActionScrolled) }
    
    override fun getLayout() = R.layout.scripts_setup_dependency
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(viewModel)
        
        viewModel.setIsNew(navArgs.isNew)
        viewModel.setDependencyId(navArgs.dependencyId)
        viewModel.setFlowViewModel(setupScriptViewModel)
        
        viewModel.close.onNavigate(this) {
            findNavController().navigateUp()
        }
    
        viewModel.conditionContainers.observe(viewLifecycleOwner, ::bindConditions)
        viewModel.actionContainers.observe(viewLifecycleOwner, ::bindActions)
        viewModel.selectionMode.observe(viewLifecycleOwner, ::bindSelectMode)
        viewModel.toolbarTitle.observe(viewLifecycleOwner, ::bindToolbarTitle)
        
        conditions_recycler.adapter = conditionsController.adapter
        conditions_recycler.setHasFixedSize(false)
        actions_recycler.adapter = actionsController.adapter
        setupPopupMenuForConditions()

        delete.visible = !viewModel.isNew
        delete.setOnClickListener { viewModel.onDeleteDependency() }
    }
    
    private fun setupPopupMenuForConditions() {
        conditions_options.setOnClickListener {
            PopupMenu(context, it).apply {
                inflate(R.menu.conditions_options)
                show()
                
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.options_add -> viewModel.addConditionsContainer()
                        R.id.options_select -> viewModel.onSelectionModeClick()
                    }
                    true
                }
            }
        }
    }
    
    private fun bindConditions(states: List<ContainerState<Condition>>) {
        conditionsController.setData(states, viewModel)
    }
    
    private fun bindActions(states: List<ContainerState<Action>>) {
        actionsController.setData(states, viewModel)
    }
    
    private fun bindSelectMode(selectMode: Boolean) {
        when (selectMode) {
            true -> {
                setCancelSelectionNavigationIcon()
                inflateToolbarDeleteMenu()
            }
            false -> {
                inflateToolbarSaveMenu()
                setCloseNavigationIcon()
            }
        }
    }
    
    override suspend fun onBackPressed(): BackPressState {
        confirmCancellation()
        return BackPressState.CONSUMED
    }
    private fun setCloseNavigationIcon() {
        toolbarController.setNavigationIcon(R.drawable.ic_close) {
            lifecycleScope.launchWhenResumed { confirmCancellation() }
        }
    }
    
    private suspend fun confirmCancellation() {
        val confirmed = confirmAction(context) {
            title = "Close without saving?"
        }
        viewModel.takeIf { confirmed }?.onCancel()
    }
    
    private fun inflateToolbarSaveMenu() {
        toolbarController.setMenu(R.menu.save) { id ->
            if (id != R.id.save) return@setMenu
            viewModel.onSave()
        }
    }
    
    private fun inflateToolbarDeleteMenu() {
        toolbarController.setMenu(R.menu.delete) { id ->
            if (id != R.id.delete) return@setMenu
            viewModel.onDeleteSelected()
        }
    }
    
    private fun setCancelSelectionNavigationIcon() {
        toolbarController.setNavigationIcon(R.drawable.ic_close) {
            viewModel.cancelSelection()
        }
    }
    
    private fun bindToolbarTitle(title: String) {
        toolbarController.setTitle(title)
    }
}

