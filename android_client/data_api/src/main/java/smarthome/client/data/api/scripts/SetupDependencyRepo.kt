package smarthome.client.data.api.scripts

import smarthome.client.entity.script.dependency.DependencyDetails
import smarthome.client.entity.script.dependency.DependencyId

interface SetupDependencyRepo {
    fun get(scriptId: Long, dependencyId: DependencyId): DependencyDetails
    fun set(scriptId: Long, dependencyDetails: DependencyDetails)
    
}