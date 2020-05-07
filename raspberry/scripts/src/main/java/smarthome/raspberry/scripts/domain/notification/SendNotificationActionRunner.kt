package smarthome.raspberry.scripts.domain.notification

import com.google.firebase.messaging.AndroidConfig
import com.google.firebase.messaging.AndroidNotification
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Action
import smarthome.raspberry.notification.api.domain.usecase.GetNotificationTokenForUserUseCase
import smarthome.raspberry.scripts.api.domain.ActionRunner
import smarthome.raspberry.scripts.api.domain.usecase.GetBlockByIdUseCase
import smarthome.raspberry.scripts.api.domain.usecase.GetScriptByContainedBlockUseCase
import smarthome.raspberry.scripts.api.domain.notification.SendNotificationAction
import smarthome.raspberry.scripts.api.domain.notification.NotificationBlock

@Component
class SendNotificationActionRunner(
        private val getBlockByIdUseCase: GetBlockByIdUseCase,
        private val getNotificationTokenForUserUseCase: GetNotificationTokenForUserUseCase,
        private val getScriptByContainedBlockUseCase: GetScriptByContainedBlockUseCase

) : ActionRunner {

    override fun runAction(action: Action, blockId: String) {
        val block = getBlockByIdUseCase.execute(blockId)

        require(action is SendNotificationAction)
        require(block is NotificationBlock)

        FirebaseMessaging.getInstance().send(Message.builder()
                .setToken(getNotificationTokenForUserUseCase.execute(action.user).token)
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .setNotification(AndroidNotification.builder()
                                .setTitle(getScriptByContainedBlockUseCase.execute(block).name.ifEmpty { "Script Action" })
                                .setBody(action.message)
                                .build()
                        )
                        .build())
                .build()
        )
    }
}