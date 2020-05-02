package smarthome.client.presentation.scripts.setup.dependency.action.notification

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import kotlinx.android.synthetic.main.send_notification_action.view.*
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.setup.dependency.DependencyUnitView
import smarthome.client.presentation.util.inflate

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class SendNotificationActionView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : DependencyUnitView(context, attrs, defStyleAttr) {

    override fun onCreateView(viewGroup: ViewGroup) {
        viewGroup.inflate(R.layout.send_notification_action)
    }

    var onChangeMessage: ((String) -> Unit)? = null @CallbackProp set
    var onChangeUser: ((String) -> Unit)? = null @CallbackProp set

    lateinit var user: CharSequence @TextProp set
    lateinit var message: CharSequence @TextProp set


    @AfterPropsSet
    fun onPropsReady() {
        if (!input_message.hasFocus()) input_message.text = message.toString()
        if (!input_user.hasFocus()) input_user.text = user.toString()

        input_message.setOnTextChanged { onChangeMessage?.invoke(it) }
        input_user.setOnTextChanged { onChangeUser?.invoke(it) }
    }
}