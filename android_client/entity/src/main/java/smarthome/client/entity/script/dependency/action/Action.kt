package smarthome.client.entity.script.dependency.action

import smarthome.client.entity.script.dependency.DependencyId

interface Action {
    val dependencyId: DependencyId
}