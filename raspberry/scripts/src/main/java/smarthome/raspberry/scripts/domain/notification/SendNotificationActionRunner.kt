package smarthome.raspberry.scripts.domain.notification

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Action
import smarthome.raspberry.scripts.api.domain.ActionRunner
import smarthome.raspberry.scripts.api.domain.GetBlockByIdUseCase
import smarthome.raspberry.scripts.api.domain.actions.SendNotificationAction
import smarthome.raspberry.scripts.api.domain.blocks.NotificationBlock

@Component
class SendNotificationActionRunner(
        private val getBlockByIdUseCase: GetBlockByIdUseCase
) : ActionRunner {

    override fun runAction(action: Action, blockId: String) {
        val block = getBlockByIdUseCase.execute(blockId)

        require(action is SendNotificationAction)
        require(block is NotificationBlock)

        
    }
}