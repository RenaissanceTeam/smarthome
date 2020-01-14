package smarthome.client.presentation.devices.deviceaddition.epoxy

import android.content.Context
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.epoxy.*
import kotlinx.android.synthetic.main.pending_device_item.view.*
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.presentation.R
import smarthome.client.presentation.controllers.controllerdetail.statechanger.extensions.inflate
import smarthome.client.presentation.visible


@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class PendingDeviceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    @ModelProp lateinit var device: GeneralDeviceInfo
    var onExpand: (() -> Unit)? = null @CallbackProp set
    var onDeviceClick: (() -> Unit)? = null @CallbackProp set
    @ModelProp lateinit var controllers: List<PendingControllerViewModel_>
    
    var isExpanded: Boolean = false @ModelProp set
    
    init {
        inflate(R.layout.pending_device_item)
    }
    
    @AfterPropsSet
    fun useProps() {
        card.setOnClickListener { onDeviceClick?.invoke() }
        expand_button.setOnClickListener { onExpand?.invoke() }
        
        name.text = device.name
        type.text = device.type
    
        animateExpandButton()
        controllers_recycler.visible = isExpanded
        controllers_recycler.setModels(controllers)
        controllers_recycler.layoutManager = GridLayoutManager(context, 2)
    }
    
    private fun animateExpandButton() {
        val rotation = when (isExpanded) {
            true -> 180f
            else -> 0f
        }
        expand_button.animate().rotation(rotation).interpolator =
            AccelerateDecelerateInterpolator()
    }
    
}