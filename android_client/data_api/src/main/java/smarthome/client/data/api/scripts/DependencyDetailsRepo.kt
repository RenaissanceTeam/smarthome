package smarthome.client.data.api.scripts

import smarthome.client.entity.script.dependency.Dependency
import smarthome.client.entity.script.dependency.DependencyDetails

interface DependencyDetailsRepo {
    fun getByDependency(dependency: Dependency): DependencyDetails?
    fun save(details: DependencyDetails)
}