package smarthome.client.scripts

import smarthome.client.scripts.actions.Action
import smarthome.client.scripts.conditions.Condition
import smarthome.library.common.GUID

data class Script(val name: String = "",
             val conditions: MutableList<Condition> = mutableListOf(),
             val actions: MutableList<Action> = mutableListOf(),
                  val guid: Long = GUID.getInstance().random())


