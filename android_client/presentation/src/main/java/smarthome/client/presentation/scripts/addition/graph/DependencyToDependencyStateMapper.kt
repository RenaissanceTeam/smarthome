package smarthome.client.presentation.scripts.addition.graph

import smarthome.client.entity.script.Dependency
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.DependencyState

class DependencyToDependencyStateMapper {
    fun map(dependency: Dependency) = DependencyState(dependency)
}