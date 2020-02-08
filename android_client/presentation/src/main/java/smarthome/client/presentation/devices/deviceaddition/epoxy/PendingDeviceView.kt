package smarthome.client.presentation.devices.deviceaddition.epoxy

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import kotlinx.android.synthetic.main.pending_device_item.view.*
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.presentation.R
import smarthome.client.presentation.util.inflate
import smarthome.client.util.visible

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class PendingDeviceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    init {
        inflate(R.layout.pending_device_item)
    }
    
    @ModelProp lateinit var device: GeneralDeviceInfo
    var onExpand: (() -> Unit)? = null @CallbackProp set
    var onDeviceLongClicked: (() -> Unit)? = null @CallbackProp set
    var onAccept: (() -> Unit)? = null @CallbackProp set
    var onDecline: (() -> Unit)? = null @CallbackProp set
    @ModelProp lateinit var controllers: List<PendingControllerViewModel_>
    
    var isExpanded: Boolean = false @ModelProp set
    var acceptInProgress: Boolean = false @ModelProp set
    var declineInProgress: Boolean = false @ModelProp set
    
    
    @AfterPropsSet
    fun useProps() {
        card.setOnLongClickListener { onDeviceLongClicked?.invoke(); true }
        expand_button.setOnClickListener { onExpand?.invoke() }
        if (!declineInProgress) accept_button.setOnClickListener { onAccept?.invoke() }
        if (!acceptInProgress) delete.setOnClickListener { onDecline?.invoke() }
        
        name.text = device.name
        type.text = device.type
    
    
        accept_button.visibility = if (acceptInProgress) View.INVISIBLE else View.VISIBLE
        delete.visibility = if (declineInProgress) View.INVISIBLE else View.VISIBLE
        
        animateExpandButton()
        
        controllers_recycler.visible = isExpanded
        controllers_recycler.setModels(controllers)
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