package smarthome.library.common.scripts

import smarthome.library.common.GUID

open class Script(val name: String = "",
                  val conditions: MutableList<Condition> = mutableListOf(),
                  val actions: MutableList<Action> = mutableListOf(),
                  val guid: Long = GUID.random())
