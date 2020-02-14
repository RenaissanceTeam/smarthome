package smarthome.client.arduino.entity.condition

import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.condition.controller.ControllerCondition
import smarthome.client.entity.script.dependency.condition.controller.ControllerValueCondition

class HumidityCondition(
    controllerId: Long,
    dependencyId: DependencyId
): ControllerValueCondition(controllerId, dependencyId)

