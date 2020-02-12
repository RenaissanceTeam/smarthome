package smarthome.client.presentation.scripts.addition.dependency

import android.os.Bundle
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.scripts_setup_dependency.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import smarthome.client.presentation.R
import smarthome.client.presentation.core.BaseFragment
import smarthome.client.presentation.main.toolbar.ToolbarController
import smarthome.client.presentation.scripts.addition.SetupScriptViewModel
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerModelsHolder
import smarthome.client.presentation.scripts.addition.dependency.container.action.ActionContainerState
import smarthome.client.presentation.scripts.addition.dependency.container.action.ActionContainersController
import smarthome.client.presentation.scripts.addition.dependency.container.condition.ConditionContainerState
import smarthome.client.presentation.scripts.addition.dependency.container.condition.ConditionContainersController
import smarthome.client.presentation.scripts.resolver.ActionModelResolver
import smarthome.client.presentation.scripts.resolver.ConditionModelResolver
import smarthome.client.presentation.util.confirmAction

class SetupDependencyFragment : BaseFragment<SetupDependencyViewModel>(SetupDependencyViewModel::class) {
    private val navArgs: SetupDependencyFragmentArgs by navArgs()
    private val toolbarController: ToolbarController by inject()
    private val setupScriptViewModel: SetupScriptViewModel by sharedViewModel()
    private val conditionsController = ConditionContainersController()
    private val actionsController = ActionContainersController()
    private val conditionModelResolver: ConditionModelResolver by inject()
    private val actionModelResolver: ActionModelResolver by inject()
    
    override fun getLayout() = R.layout.scripts_setup_dependency
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    
        viewModel.setIsNew(navArgs.isNew)
        viewModel.setDependencyId(navArgs.dependencyId)
        viewModel.setFlowViewModel(setupScriptViewModel)
        
        toolbarController.setMenu(R.menu.save) { id ->
            if (id != R.id.save) return@setMenu
            TODO()
        }
    
        toolbarController.setNavigationIcon(R.drawable.ic_close) {
            launchInViewScope {
                val confirmed = confirmAction(context) {
                    title = "Close without saving?"
                }
                viewModel.takeIf { confirmed }?.onCancel()
            }
        }
        
        viewModel.close.onNavigate(this) {
            findNavController().popBackStack()
        }
    
        viewModel.conditions.observe(this, ::bindConditions)
    
        viewModel.action.observe(this, ::bindActions)
        
        conditions_recycler.adapter = conditionsController.adapter
        actions_recycler.adapter = actionsController.adapter
    }
    
    private fun bindConditions(states: List<ConditionContainerState>) {
        conditionsController.setData(states.map { containerState ->
            ContainerModelsHolder(
                containerState.id,
                containerState.conditions.map(conditionModelResolver::resolve)
            )
        })
    }
    
    private fun bindActions(states: List<ActionContainerState>) {
        actionsController.setData(states.map { containerState ->
            ContainerModelsHolder(
                containerState.id,
                containerState.actions.map(actionModelResolver::resolve)
            )
        })
    }
}

