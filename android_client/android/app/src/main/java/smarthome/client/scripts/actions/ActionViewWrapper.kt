package smarthome.client.scripts.actions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import smarthome.client.ACTION_READ_CONTROLLER
import smarthome.client.ACTION_WRITE_CONTROLLER
import smarthome.library.common.constants.*
import smarthome.library.common.scripts.actions.Action
import smarthome.library.common.scripts.actions.ReadAction
import smarthome.library.common.scripts.actions.WriteAction
import smarthome.library.common.scripts.conditions.Condition

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