package smarthome.client.presentation.scripts.addition.dependency.action

import smarthome.client.entity.script.dependency.action.Action

interface ActionView {
    fun setAction(action: Action)
    fun getAction(): Action
}