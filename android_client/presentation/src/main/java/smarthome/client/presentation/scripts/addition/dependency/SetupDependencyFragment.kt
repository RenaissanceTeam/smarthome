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
import smarthome.client.presentation.scripts.resolver.ConditionViewResolver
import smarthome.client.presentation.util.confirmAction

class SetupDependencyFragment : BaseFragment<SetupDependencyViewModel>(SetupDependencyViewModel::class) {
    private val navArgs: SetupDependencyFragmentArgs by navArgs()
    private val toolbarController: ToolbarController by inject()
    private val conditionViewResolver: ConditionViewResolver by inject()
    private val setupScriptViewModel: SetupScriptViewModel by sharedViewModel()
    private val containerViews = mutableMapOf<ConditionContainerState, ConditionViewContainer>()
    
    override fun getLayout() = R.layout.scripts_setup_dependency
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    
        viewModel.setIsNew(navArgs.isNew)
        viewModel.setDependencyId(navArgs.dependencyId)
        viewModel.setFlowViewModel(setupScriptViewModel)
        
        toolbarController.setMenu(R.menu.save) { id ->
            if (id != R.id.save) return@setMenu
        
            viewModel.onSave()
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
    
        viewModel.containers.observe(this) { containers ->
            retainOnlyPostedConditionContainers(containers)
            containers.forEach(::inflateContainerIfNeeded)
        }
    }
    
    private fun inflateContainerIfNeeded(container: ConditionContainerState) {
        if (containerViews.containsKey(container)) return
        
        context?.let { context ->
            ConditionViewContainer(context)
                .also(conditions_container::addView)
                .also { it.setConditions(viewModel.getEmptyConditions()) }
                .also { containerViews[container] = it }
        }
    }
    
    private fun retainOnlyPostedConditionContainers(states: List<ConditionContainerState>) {
        (containerViews.keys - states).forEach {
            conditions_container.removeView(containerViews[it])
            containerViews.remove(it)
        }
    }
}

