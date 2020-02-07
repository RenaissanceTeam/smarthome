package smarthome.client.presentation.scripts.addition.graph.mapper

import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.DependencyState

class DependencyToDependencyStateMapper {
    fun map(dependency: Dependency) = DependencyState(dependency)
}