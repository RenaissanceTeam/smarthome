package smarthome.client.presentation.scripts.addition.dependency

import android.os.Bundle
import android.widget.PopupMenu
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.scripts_setup_dependency.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.ACTION_CONTAINER_CONTROLLER
import smarthome.client.presentation.CONDITION_CONTAINER_CONTROLLER
import smarthome.client.presentation.R
import smarthome.client.presentation.core.BaseFragment
import smarthome.client.presentation.main.toolbar.ToolbarController
import smarthome.client.presentation.scripts.addition.SetupScriptViewModel
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerState
import smarthome.client.presentation.scripts.addition.dependency.container.ContainersController
import smarthome.client.presentation.util.confirmAction

class SetupDependencyFragment : BaseFragment<SetupDependencyViewModel>(SetupDependencyViewModel::class) {
    private val navArgs: SetupDependencyFragmentArgs by navArgs()
    private val toolbarController: ToolbarController by inject()
    private val setupScriptViewModel: SetupScriptViewModel by sharedViewModel()
    private val conditionsController: ContainersController<Condition>
        by inject(named(CONDITION_CONTAINER_CONTROLLER)) { parametersOf(viewModel::onConditionScrolled) }
    private val actionsController: ContainersController<Action>
        by inject(named(ACTION_CONTAINER_CONTROLLER)) { parametersOf(viewModel::onActionScrolled) }
    
    override fun getLayout() = R.layout.scripts_setup_dependency
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    
        viewModel.setIsNew(navArgs.isNew)
        viewModel.setDependencyId(navArgs.dependencyId)
        viewModel.setFlowViewModel(setupScriptViewModel)
        
        
        
        viewModel.close.onNavigate(this) {
            findNavController().popBackStack()
        }
    
        viewModel.conditionContainers.observe(this, ::bindConditions)
        viewModel.actionContainers.observe(this, ::bindActions)
        viewModel.selectionMode.observe(this, ::bindSelectMode)
        viewModel.toolbarTitle.observe(this, ::bindToolbarTitle)
        
        conditions_recycler.adapter = conditionsController.adapter
        actions_recycler.adapter = actionsController.adapter
        setupPopupMenuForConditions()
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
    
    private fun setCloseNavigationIcon() {
        toolbarController.setNavigationIcon(R.drawable.ic_close) {
            launchInViewScope {
                val confirmed = confirmAction(context) {
                    title = "Close without saving?"
                }
                viewModel.takeIf { confirmed }?.onCancel()
            }
        }
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

