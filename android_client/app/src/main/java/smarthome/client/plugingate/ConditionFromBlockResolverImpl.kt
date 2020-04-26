package smarthome.client.plugingate

import smarthome.client.domain.api.scripts.resolver.ConditionFromBlockResolver
import smarthome.client.domain.api.scripts.resolver.Resolver
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.dependency.action.Action
import smarthome.client.entity.script.dependency.condition.Condition

class ConditionFromBlockResolverImpl(
    private val resolvers: List<ConditionFromBlockResolver>
): Resolver<Block, List<Condition>> by ResolverFinder(resolvers), ConditionFromBlockResolver