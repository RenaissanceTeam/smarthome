package smarthome.client.presentation.screens.scripts.conditions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import smarthome.client.CONDITION_CONTROLLER
import smarthome.client.CONDITION_EXACT_TIME
import smarthome.client.CONDITION_INTERVAL_TIME
import smarthome.library.common.scripts.conditions.Condition
import smarthome.library.common.scripts.conditions.ControllerCondition
import smarthome.library.common.scripts.conditions.ExactTimeCondition
import smarthome.library.common.scripts.conditions.IntervalTimeCondition

interface ConditionViewWrapper {
    companion object {
        fun withTag(tag: String): Condition {
            return when (tag) {
                CONDITION_CONTROLLER -> ControllerCondition()
                CONDITION_EXACT_TIME -> ExactTimeCondition()
                CONDITION_INTERVAL_TIME -> IntervalTimeCondition()
                else -> throw RuntimeException("No condition view wrapper with tag $tag")
            }
        }

        fun wrap(condition: Condition, provider: AllConditionsProvider): ConditionViewWrapper {
            return when (condition) {
                is ControllerCondition -> ControllerConditionViewWrapper(provider, condition)
                is ExactTimeCondition -> ExactTimeConditionViewWrapper(condition)
                is IntervalTimeCondition -> IntervalTimeConditionViewWrapper(condition)
                else -> throw RuntimeException("No condition view wrapper for condition: $condition")
            }
        }
    }


    fun getTag(): String

    fun getView(root: ViewGroup): View

    fun isFilled(): Boolean

    fun inflateLayout(root: ViewGroup, layout: Int): View {
        val inflater = LayoutInflater.from(root.context)
        return inflater.inflate(layout, root, false)
    }
}