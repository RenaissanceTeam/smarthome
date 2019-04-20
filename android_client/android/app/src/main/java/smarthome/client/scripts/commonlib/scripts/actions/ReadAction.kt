package smarthome.client.scripts.commonlib.scripts.actions

import smarthome.client.scripts.commonlib.scripts.actions.Action
import smarthome.library.common.BaseController


open class ReadAction : Action() {
    var chosenController: BaseController? = null

    override fun toString() = "Read ${chosenController?.name}"
}

