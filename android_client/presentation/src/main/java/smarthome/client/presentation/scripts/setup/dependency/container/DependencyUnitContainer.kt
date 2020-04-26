package smarthome.client.presentation.scripts.setup.dependency.container

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import kotlinx.android.synthetic.main.scripts_dependency_units_container.view.*
import smarthome.client.presentation.R
import smarthome.client.presentation.util.inflate
import smarthome.client.util.visible


@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class DependencyUnitContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    init {
        inflate(R.layout.scripts_dependency_units_container)
    }
    
    @ModelProp
    lateinit var dependencyUnitModels: List<EpoxyModel<*>>
    var selectionMode: Boolean = false @ModelProp set
    var select: Boolean = false @ModelProp set
    var onSelect: ((Boolean) -> Unit)? = null @CallbackProp set
    var onScrolled: ((Int) -> Unit)? = null @CallbackProp set
    
    @AfterPropsSet
    fun onPropsReady() {
        select_container.visible = selectionMode
        isSelected = select
        select_container.isChecked = select
        select_container.setOnCheckedChangeListener { _, isChecked -> onSelect?.invoke(isChecked) }
    
        dependency_units_carousel.onScrolled = onScrolled
        dependency_units_carousel.setModels(dependencyUnitModels)
        dependency_units_carousel.numViewsToShowOnScreen = 1.05f
    }
}