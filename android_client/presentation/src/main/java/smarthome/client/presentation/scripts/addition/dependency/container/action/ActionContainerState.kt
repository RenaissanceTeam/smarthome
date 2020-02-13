package smarthome.client.presentation.scripts.addition.dependency.container.action

import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerData
import smarthome.client.presentation.scripts.addition.dependency.container.ContainerId

data class ActionContainerData(val actions: List<Action>): ContainerData