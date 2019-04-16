package smarthome.client.scripts.actions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import smarthome.client.ACTION_READ_CONTROLLER
import smarthome.client.ACTION_WRITE_CONTROLLER

abstract class Action {
    companion object {
        fun withTag(tag: String, provider: AllActionsProvider): Action {
            return when (tag) {
                ACTION_READ_CONTROLLER -> ReadAction(provider)
                ACTION_WRITE_CONTROLLER -> WriteAction(provider)
                else -> throw RuntimeException("No action with tag $tag found")
            }
        }
    }

    abstract fun getView(root: ViewGroup): View

    abstract fun isFilled(): Boolean

    abstract fun getTag(): String

    internal fun inflateLayout(root: ViewGroup, layout: Int): View {
        val inflater = LayoutInflater.from(root.context)
        return inflater.inflate(layout, root, false)
    }
}