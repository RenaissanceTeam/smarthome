package smarthome.client.domain.scripts.blocks.location

import smarthome.client.domain.api.scripts.blocks.location.HomeGeofenceCondition
import smarthome.client.domain.api.scripts.resolver.ConditionFromBlockResolver
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.block.LocationBlock
import smarthome.client.entity.script.dependency.condition.Condition
import smarthome.client.util.generateId

class LocationConditionFromBlockResolver : ConditionFromBlockResolver {
    override fun canResolve(item: Block): Boolean {
        return item is LocationBlock
    }

    override fun resolve(item: Block): List<Condition> {
        return listOf(
                HomeGeofenceCondition(generateId())
        )
    }
}