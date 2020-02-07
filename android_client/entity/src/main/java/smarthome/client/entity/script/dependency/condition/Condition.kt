package smarthome.client.entity.script.dependency.condition

import smarthome.client.entity.script.dependency.DependencyId

interface Condition {
    val dependencyId: DependencyId
}