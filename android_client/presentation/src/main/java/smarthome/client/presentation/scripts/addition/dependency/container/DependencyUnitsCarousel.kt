package smarthome.client.presentation.scripts.addition.dependency.container

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearSnapHelper
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.ModelView
import smarthome.client.presentation.util.SnapOnScrollListener

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class DependencyUnitsCarousel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Carousel(context, attrs, defStyleAttr) {
    
    var onScrolled: ((Int) -> Unit)? = null @CallbackProp set
    
    init {
        addOnScrollListener(SnapOnScrollListener(snapHelper) {
            onScrolled?.invoke(it)
        })
    }
    
    companion object {
        // WARNING!!! this is not set to Carousel, i've just created this one object for
        // finding center of currently snapped view. So it's just a helper singleton
        // So it won't take any effect on snapping behavior when we change this variable
        //
        // Setting default snap factory on Carousel didn't work and idk why..
        private val snapHelper = LinearSnapHelper()
    }
}