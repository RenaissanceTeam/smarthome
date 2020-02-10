package smarthome.client.presentation.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import smarthome.client.presentation.util.KoinViewModel
import kotlin.reflect.KClass

abstract class BaseFragment<VM : KoinViewModel>(viewModelType: KClass<VM>) : Fragment() {
    protected val viewModel: VM by createViewModelLazy(viewModelType, { viewModelStore }, null)
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)
    
    abstract fun getLayout(): Int
    
    protected fun launchInViewScope(block: suspend () -> Unit) {
        scope.launch { block.invoke() }
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayout(), container, false)
    }
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        lifecycle.addObserver(viewModel)
    }
}