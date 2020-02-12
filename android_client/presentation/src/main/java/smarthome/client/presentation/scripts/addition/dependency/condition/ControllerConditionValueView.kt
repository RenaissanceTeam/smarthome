package smarthome.client.presentation.scripts.addition.dependency.condition

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.scripts_value_condition.view.*
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.entity.script.dependency.condition.controller.ControllerValueConditionData
import smarthome.client.entity.script.dependency.condition.controller.ValueSigns
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.addition.dependency.DependencyUnitView
import smarthome.client.presentation.util.inflate

open class ControllerConditionValueView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DependencyUnitView(context, attrs, defStyleAttr), ConditionView {
    private var condition: Condition? = null
    
    override fun onCreateView(viewGroup: ViewGroup) {
        viewGroup.inflate(R.layout.scripts_value_condition)
    }
    
    override fun onViewCreated(view: View) {
//        setOnSignChanged { newSign ->
//            val beforeChange = condition ?: return@setOnSignChanged
//            condition = beforeChange.withSign(newSign)
//        }
//
//        setOnValueChanged { newValue ->
//            val beforeChange = condition ?: return@setOnValueChanged
//            condition = beforeChange.withValue(newValue)
//        }
    }
    
    var title: CharSequence
        get() = title_value.text.toString()
        set(value) {
            title_value.text = value
        }
    
    override fun setCondition(condition: Condition) {
        if (condition.data !is ControllerValueConditionData) return
        this.condition = condition
    }
    
    override fun getCondition(): Condition {
        return condition ?: throw IllegalStateException("No condition has been set for $this")
    }
    
    private fun setOnSignChanged(listener: (String) -> Unit) {
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
    
    private fun setOnValueChanged(listener: (String) -> Unit) {
        value_input.setOnTextChanged(listener)
    }
}