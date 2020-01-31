package smarthome.client.presentation.components

import android.content.Context
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import com.airbnb.epoxy.ModelView
import kotlinx.android.synthetic.main.empty_item.view.*
import smarthome.client.presentation.R
import smarthome.client.presentation.controllers.controllerdetail.statechanger.extensions.inflate

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_MATCH_HEIGHT)
class EmptyItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        val view = inflate(R.layout.empty_item)
    
        RotateAnimation(0f, 720f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
            0.5f).apply {
            duration = 5000
            interpolator = AccelerateDecelerateInterpolator()
            repeatCount = Animation.INFINITE
        }.also(view.empty_image::startAnimation)
    }
}