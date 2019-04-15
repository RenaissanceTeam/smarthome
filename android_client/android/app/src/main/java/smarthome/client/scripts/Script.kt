package smarthome.client.scripts

import smarthome.client.scripts.actions.Action
import smarthome.client.scripts.conditions.Condition

data class Script(val name: String,
             val conditions: MutableList<Condition>,
             val actions: MutableList<Action>)


