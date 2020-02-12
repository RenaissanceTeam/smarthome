package smarthome.client.presentation.scripts.addition.dependency.container.action

import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerId

data class ActionContainerState(val id: ContainerId,
                                val actions: List<Action>,
                                val selected: Int)