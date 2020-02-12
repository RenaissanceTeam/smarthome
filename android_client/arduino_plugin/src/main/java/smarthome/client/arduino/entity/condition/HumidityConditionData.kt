package smarthome.client.arduino.entity.condition

import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.condition.controller.ControllerValueConditionData

class HumidityConditionData(
    controllerId: Long,
    dependencyId: DependencyId
): ControllerValueConditionData(controllerId, dependencyId)

