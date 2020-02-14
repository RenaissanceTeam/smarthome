package smarthome.client.plugingate

import smarthome.client.domain.api.scripts.resolver.ActionFromBlockResolver
import smarthome.client.domain.api.scripts.resolver.Resolver
import smarthome.client.entity.script.block.Block
import smarthome.client.entity.script.dependency.action.Action

class ActionFromBlockResolverImpl(
    private val resolvers: List<ActionFromBlockResolver>
) : Resolver<Block, List<Action>> by ResolverFinder(resolvers), ActionFromBlockResolver