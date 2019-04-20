package smarthome.client.scripts.actions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import smarthome.client.ACTION_READ_CONTROLLER
import smarthome.client.ACTION_WRITE_CONTROLLER
import smarthome.client.scripts.commonlib.scripts.actions.Action
import smarthome.client.scripts.commonlib.scripts.actions.ReadAction
import smarthome.client.scripts.commonlib.scripts.actions.WriteAction
import smarthome.client.scripts.commonlib.scripts.conditions.Condition

interface ActionViewWrapper {
    companion object {
        fun withTag(tag: String): Action {
            return when (tag) {
                ACTION_READ_CONTROLLER -> ReadAction()
                ACTION_WRITE_CONTROLLER -> WriteAction()
                else -> throw RuntimeException("No action with tag $tag found")
            }
        }

        fun wrap(action: Action, provider: AllActionsProvider): ActionViewWrapper {
            return when (action) {
                is ReadAction -> ReadActionViewWrapper(provider, action)
                is WriteAction -> WriteActionViewWrapper(provider, action)
                else -> throw RuntimeException("No action wrapper for $action found")
            }
        }
    }

    fun getView(root: ViewGroup): View

    fun isFilled(): Boolean

    fun inflateLayout(root: ViewGroup, layout: Int): View {
        val inflater = LayoutInflater.from(root.context)
        return inflater.inflate(layout, root, false)
    }

    fun getTag(): String

}