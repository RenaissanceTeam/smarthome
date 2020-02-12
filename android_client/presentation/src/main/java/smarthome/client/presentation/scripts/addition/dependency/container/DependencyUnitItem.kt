package smarthome.client.presentation.scripts.addition.dependency.container

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import smarthome.client.presentation.R
import smarthome.client.presentation.util.inflate

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class DependencyUnitItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    init {
        inflate(R.layout.scripts_dependency_unit_wrapper)
    }
    
    @ModelProp lateinit var item: EpoxyModel<*>
    
    @AfterPropsSet
    fun onPropsReady() {
    
    }
 }