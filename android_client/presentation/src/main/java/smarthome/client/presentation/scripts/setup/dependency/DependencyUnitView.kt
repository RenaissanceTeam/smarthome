package smarthome.client.presentation.scripts.setup.dependency

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.core.view.children
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

        unit_wrapper.children.forEach { childView ->
            (childView.layoutParams as? RelativeLayout.LayoutParams)
                    ?.apply { addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE) }
                    ?.let { childView.layoutParams = it }
        }
    }


    abstract fun onCreateView(viewGroup: ViewGroup)
    open fun onViewCreated(view: View) {}
}