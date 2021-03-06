package smarthome.client.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.ModelView
import smarthome.client.presentation.R
import smarthome.client.presentation.util.inflate

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class DividerItem @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        inflate(R.layout.divider_item)
    }
}