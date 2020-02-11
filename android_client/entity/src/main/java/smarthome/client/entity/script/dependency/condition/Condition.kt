package smarthome.client.entity.script.dependency.condition

import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.DependencyUnit

interface Condition: DependencyUnit {
    val dependencyId: DependencyId
    
    
}