package smarthome.client.presentation.scripts.addition.dependency.condition

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import kotlinx.android.synthetic.main.scripts_value_condition.view.*
import smarthome.client.entity.script.dependency.condition.controller.ValueSigns
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.addition.dependency.DependencyUnitView
import smarthome.client.presentation.util.inflate
import smarthome.client.util.log

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ControllerConditionValueView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DependencyUnitView(context, attrs, defStyleAttr) {
    
    override fun onCreateView(viewGroup: ViewGroup) {
        viewGroup.inflate(R.layout.scripts_value_condition)
    }
    
    var title: String? = null @ModelProp set
    var sign: String? = null @ModelProp set
    var value: String? = null @ModelProp set
    
    
    var onSignChanged: ((String) -> Unit)? = null @CallbackProp set
    var onValueChanged: ((String) -> Unit)? = null @CallbackProp set
    
    @AfterPropsSet
    fun onPropsReady() {
        signs.setOnCheckedChangeListener(null)
        value_input.removeTextChangedListeners()

        bindTitleText()
        bindValueText()
        bindSign()
        
        listenToSignChanges()
        listenToValueChanges()
    }
    
    private fun bindTitleText() {
        if (title != title_value.text) title_value.text = "${title.orEmpty()} focus=${hasFocus()}, $sign, $value"
    }
    
    private fun bindSign() {
        if (sign == null) {
            signs.clearCheck()
            return
        }
        
        val toCheck = when (sign) {
            ValueSigns.less -> R.id.less
            ValueSigns.more -> R.id.more
            ValueSigns.equal -> R.id.equal
            else -> -1
        }
        if (toCheck != signs.checkedRadioButtonId) signs.check(toCheck)
    }
    
    private fun bindValueText() {
        if (hasFocus()) return
        
        value_input.text = value.orEmpty()
    }
    
    private fun listenToValueChanges() {
        value_input.setOnTextChanged { onValueChanged?.invoke(it) }
    }
    
    private fun listenToSignChanges() {
        signs.setOnCheckedChangeListener { _, checkedId ->
            val checkedSign = when (checkedId) {
                R.id.less -> ValueSigns.less
                R.id.more -> ValueSigns.more
                R.id.equal -> ValueSigns.equal
                else -> return@setOnCheckedChangeListener
            }
            onSignChanged?.invoke(checkedSign)
        }
    }
}