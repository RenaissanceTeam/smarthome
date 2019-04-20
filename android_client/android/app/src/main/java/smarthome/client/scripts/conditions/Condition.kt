package smarthome.client.scripts.conditions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import smarthome.client.CONDITION_CONTROLLER
import smarthome.client.CONDITION_EXACT_TIME
import smarthome.client.CONDITION_INTERVAL_TIME

interface ConditionViewBuilder {
    fun getTag(): String

    fun getView(root: ViewGroup): View

    fun isFilled(): Boolean
}

abstract class Condition {
    companion object {
        fun withTag(tag: String, provider: AllConditionsProvider): Condition {
            return when (tag) {
                CONDITION_CONTROLLER -> ControllerCondition(provider)
                CONDITION_EXACT_TIME -> ExactTimeCondition()
                CONDITION_INTERVAL_TIME -> IntervalTimeCondition()
                else -> throw RuntimeException("No condition with tag $tag")
            }
        }
    }

    abstract fun evaluate(): Boolean
}

fun inflateLayout(root: ViewGroup, layout: Int): View {
    val inflater = LayoutInflater.from(root.context)
    return inflater.inflate(layout, root, false)
}