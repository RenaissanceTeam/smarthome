package smarthome.client.domain.api.scripts.resolver

import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.entity.script.dependency.action.Action

interface ActionFromBlockResolver {
    fun resolve(block: Block): List<Action>
    fun canResolve(block: Block): Boolean
}