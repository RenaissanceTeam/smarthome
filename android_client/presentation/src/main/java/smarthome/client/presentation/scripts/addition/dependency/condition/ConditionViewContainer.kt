package smarthome.client.presentation.scripts.addition.dependency.condition

import android.content.Context
import android.util.AttributeSet
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.presentation.scripts.addition.dependency.container.DependencyUnitContainer
import smarthome.client.presentation.scripts.resolver.ConditionViewResolver

class ConditionViewContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DependencyUnitContainer<Condition, ConditionView>(context, attrs), KoinComponent {
    
    private val viewResolver: ConditionViewResolver by inject()
    
    override fun inflateItem(context: Context, condition: Condition): ConditionView? {
        return viewResolver.resolve(context, condition)
            ?.also { it.setCondition(condition) }
    }
}