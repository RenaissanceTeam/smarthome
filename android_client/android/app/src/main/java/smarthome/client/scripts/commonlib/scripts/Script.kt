package smarthome.client.scripts.commonlib.scripts

import smarthome.client.scripts.commonlib.scripts.conditions.Condition
import smarthome.client.scripts.commonlib.scripts.actions.Action
import smarthome.library.common.GUID

data class Script(val name: String = "",
                  val conditions: MutableList<Condition> = mutableListOf(),
                  val actions: MutableList<Action> = mutableListOf(),
                  val guid: Long = GUID.getInstance().random())
