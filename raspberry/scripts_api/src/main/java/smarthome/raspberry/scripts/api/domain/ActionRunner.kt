package smarthome.raspberry.scripts.api.domain

import smarthome.raspberry.entity.script.Action
import smarthome.raspberry.entity.script.Block
import java.util.*

interface ActionRunner {
    fun runAction(
            action: Action,
            blockId: String
    )
}