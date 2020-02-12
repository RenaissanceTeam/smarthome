package smarthome.client.presentation.scripts.addition.dependency.container

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import kotlinx.android.synthetic.main.scripts_dependency_units_container.view.*
import smarthome.client.presentation.R
import smarthome.client.presentation.util.inflate

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
    
    @AfterPropsSet
    fun onPropsReady() {
        dependency_units_carousel.setModels(dependencyUnitModels)
        dependency_units_carousel.numViewsToShowOnScreen = 1.05f
    }
}