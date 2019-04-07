package smarthome.raspberry.model.scripts

import smarthome.library.common.BaseController
import smarthome.library.common.GUID

class Script {
    val guid = 1L
    var name = ""
    val rules = listOf<Rule>()
}

class ScriptScheduler {
    val scripts = listOf<Script>()

    fun handleStateChange() {
        scripts.forEach { it.rules.forEach { it.predicate } }
    }


}

class Rule(val predicate: Predicate, val action: Action) {
}

abstract class Predicate {
    abstract fun evaluate(): Boolean
}

abstract class Action(val controller: BaseController) {
    abstract fun perform()
}