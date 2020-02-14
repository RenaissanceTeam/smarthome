package smarthome.client.plugingate

import smarthome.client.domain.api.scripts.resolver.BlockNameResolver
import smarthome.client.domain.api.scripts.resolver.Resolver
import smarthome.client.entity.script.block.Block

class BlockNameResolverImp(resolvers: List<BlockNameResolver>)
    : Resolver<Block, String> by ResolverFinder(resolvers), BlockNameResolver