package smarthome.client.presentation.scripts.addition.graph.controllers.epoxy

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.*
import kotlinx.android.synthetic.main.scripts_device_group_item.view.*
import smarthome.client.presentation.R
import smarthome.client.presentation.util.inflate

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class DeviceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    init {
        inflate(R.layout.scripts_device_group_item)
        controllers_items.layoutManager = LinearLayoutManager(context)
        controllers_items.setItemSpacingDp(4)
    }
    
    lateinit var deviceName: CharSequence @TextProp set
    @ModelProp lateinit var controllers: List<ControllerViewModel_>
    
    @AfterPropsSet
    fun onPropsReady() {
        device_name.text = deviceName
        controllers_items.setModels(controllers)
    }
 }
