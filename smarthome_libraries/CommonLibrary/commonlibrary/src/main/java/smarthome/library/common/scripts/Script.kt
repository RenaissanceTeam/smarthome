package smarthome.library.common.scripts

import smarthome.library.common.scripts.conditions.Condition
import smarthome.library.common.scripts.actions.Action
import smarthome.library.common.GUID

data class Script(val name: String = "",
                  val conditions: MutableList<Condition> = mutableListOf(),
                  val actions: MutableList<Action> = mutableListOf(),
                  val guid: Long = GUID.getInstance().random())
