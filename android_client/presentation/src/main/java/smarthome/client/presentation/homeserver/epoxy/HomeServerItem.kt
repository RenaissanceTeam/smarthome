package smarthome.client.presentation.homeserver.epoxy

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import kotlinx.android.synthetic.main.homeserver_item.view.*
import smarthome.client.presentation.R
import smarthome.client.presentation.util.inflate

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class HomeServerItem @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        inflate(R.layout.homeserver_item)
    }

    lateinit var url: CharSequence @TextProp set

    var onClick: (() -> Unit)? = null @CallbackProp set

    @AfterPropsSet
    fun onPropsReady() {
        item_url.text = url
        setOnClickListener { onClick?.invoke() }
    }
}