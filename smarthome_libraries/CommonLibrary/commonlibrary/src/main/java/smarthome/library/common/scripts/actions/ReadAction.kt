package smarthome.library.common.scripts.actions

import smarthome.library.common.scripts.actions.Action
import smarthome.library.common.BaseController


open class ReadAction : Action() {
    var chosenController: BaseController? = null

    override fun toString() = "Read ${chosenController?.name}"
}

