package smarthome.client.scripts.conditions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import smarthome.client.CONDITION_CONTROLLER
import smarthome.client.CONDITION_EXACT_TIME
import smarthome.client.CONDITION_INTERVAL_TIME

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

    abstract fun getView(root: ViewGroup): View

    abstract fun getTag(): String

    abstract fun evaluate(): Boolean

    internal fun inflateLayout(root: ViewGroup, layout: Int): View {
        val inflater = LayoutInflater.from(root.context)
        return inflater.inflate(layout, root, false)
    }
}