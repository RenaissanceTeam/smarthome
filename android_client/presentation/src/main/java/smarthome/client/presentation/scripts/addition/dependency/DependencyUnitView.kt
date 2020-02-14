package smarthome.client.presentation.scripts.addition.dependency

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.scripts_dependency_unit_wrapper.view.*
import smarthome.client.presentation.R
import smarthome.client.presentation.util.inflate

abstract class DependencyUnitView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    init {
        val view = inflate(R.layout.scripts_dependency_unit_wrapper)
        onCreateView(unit_wrapper)
        onViewCreated(view)
    }
    
    abstract fun onCreateView(viewGroup: ViewGroup)
    open fun onViewCreated(view: View) {}
}