package smarthome.client.scripts.commonlib.scripts.actions

import smarthome.client.scripts.commonlib.scripts.actions.Action
import smarthome.library.common.BaseController

open class WriteAction : Action() {
    var chosenController: BaseController? = null
    var value: String? = null

    override fun toString() = "Write $value to ${chosenController?.name}"
}