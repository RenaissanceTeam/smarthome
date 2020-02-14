package smarthome.client.presentation.scripts.addition.dependency.condition

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.scripts_value_condition.view.*
import smarthome.client.entity.script.dependency.condition.controller.ValueSigns
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.addition.dependency.condition.ConditionView
import smarthome.client.presentation.util.inflate

open class ConditionValueView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ConditionView {
    
    init {
        inflate(R.layout.scripts_value_condition)
    }
    
    
    var title: CharSequence
        get() = title_value.text.toString()
        set(value) {
            title_value.text = value
        }
    
    fun setOnSignChanged(listener: (String) -> Unit) {
        signs.setOnCheckedChangeListener { group, checkedId ->
            val checkedSign = when (checkedId) {
                R.id.less -> ValueSigns.less
                R.id.more -> ValueSigns.more
                R.id.equal -> ValueSigns.equal
                else -> return@setOnCheckedChangeListener
            }
            listener(checkedSign)
        }
    }
    
    fun setOnValueChanged(listener: (String) -> Unit) {
        value_input.setOnTextChanged(listener)
    }
}