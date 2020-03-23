package smarthome.client.presentation.scripts.setup.graph.mapper

import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.presentation.scripts.setup.graph.blockviews.dependency.DependencyState

class DependencyToDependencyStateMapper {
    fun map(dependency: Dependency) = DependencyState(dependency)
}