package smarthome.client

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class Script(val name: String,
             val conditions: MutableList<Condition>,
             val actions: MutableList<Action>)

abstract class Condition {
    companion object {
        fun withTitle(title: String): Condition {
            return when (title) {
                "Controller" -> ControllerCondition()
                "Exact Time" -> ExactTimeCondition()
                else -> throw RuntimeException("No condition with title $title")
            }
        }
    }

    abstract fun getTag(): String
    abstract fun evaluate(): Boolean
    abstract fun getFields(): List<ConditionField>
}




class ControllerCondition: Condition() {
    override fun getTag() = "Controller"

    override fun getFields(): List<ConditionField> {
        return listOf(
                TextConditionField("mock before", "mock after"),
                TextConditionField("mock2 before", "mock2 after")
                )
    }

    override fun evaluate(): Boolean = true
    override fun toString() = "'GarageMovementSensor' is triggered"
}

class ExactTimeCondition: Condition() {
    override fun getTag() = "Exact Time"

    override fun getFields(): List<ConditionField> {
        return listOf(
                TextConditionField("time before", "mock after"),
                TextConditionField("time2 before", "mock2 after")
        )
    }

    override fun evaluate(): Boolean = true
    override fun toString() = "'ExactTime' is triggered"
}

abstract class ConditionField {
    var state: String = ""
    abstract fun getView(root: ViewGroup): View
}

class TextConditionField(private val before: String,
                         private val after: String): ConditionField() {
    override fun getView(root: ViewGroup): View {
        val inflater = LayoutInflater.from(root.context)
        val view = inflater.inflate(R.layout.field_text_input, root, false)
        view.findViewById<TextView>(R.id.field_before).text = before
        view.findViewById<TextView>(R.id.field_after).text = after
        return view
    }
}

abstract class Action {
    abstract fun action()
}

class MockAction: Action() {
    override fun action() {}
    override fun toString() = "Turn on 'GarageLight'"
}

