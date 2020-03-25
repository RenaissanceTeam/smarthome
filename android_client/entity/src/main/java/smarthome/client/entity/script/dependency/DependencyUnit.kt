package smarthome.client.entity.script.dependency

import smarthome.client.entity.script.dependency.condition.DependencyUnitId

interface DependencyUnit {
    val id: DependencyUnitId?
    val data: DependencyUnitData
}