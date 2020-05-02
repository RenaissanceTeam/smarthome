package smarthome.client.presentation.scripts.setup.dependency.action.notification

import com.airbnb.epoxy.EpoxyModel
import smarthome.client.domain.api.scripts.blocks.SendNotificationAction
import smarthome.client.domain.api.scripts.usecases.dependency.ChangeSetupDependencyActionUseCase
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.presentation.scripts.resolver.ActionModelResolver

class SendNotificationActionModelResolver(
        private val changeSetupDependencyActionUseCase: ChangeSetupDependencyActionUseCase
) : ActionModelResolver {
    override fun canResolve(item: Action): Boolean {
        return item is SendNotificationAction
    }

    override fun resolve(item: Action): EpoxyModel<*> {
        require(item is SendNotificationAction)

        return SendNotificationActionViewModel_().apply {
            id(item.id.hashCode())
            user(item.user.orEmpty())
            message(item.message.orEmpty())

            onChangeUser { newUser ->
                changeSetupDependencyActionUseCase.execute(item.id) {
                    (it as? SendNotificationAction)?.copy(user = newUser) ?: it
                }
            }

            onChangeMessage { newMessage ->
                changeSetupDependencyActionUseCase.execute(item.id) {
                    (it as? SendNotificationAction)?.copy(message = newMessage) ?: it
                }
            }
        }
    }
}