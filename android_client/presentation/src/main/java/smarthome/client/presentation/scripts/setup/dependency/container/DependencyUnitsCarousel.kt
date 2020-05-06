package smarthome.client.presentation.scripts.setup.dependency.container

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearSnapHelper
import com.airbnb.epoxy.Carousel
import smarthome.client.presentation.util.SnapOnScrollListener

class DependencyUnitsCarousel @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : Carousel(context, attrs, defStyleAttr) {

    private var listener: OnScrollListener? = null

    init {
        setHasFixedSize(false)
    }

    var onScrolled: ((Int) -> Unit)? = null
        set(value) {
            field = value

            listener?.let { removeOnScrollListener(it) }
            listener = SnapOnScrollListener(snapHelper) { value?.invoke(it) }
                    .also(this@DependencyUnitsCarousel::addOnScrollListener)
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