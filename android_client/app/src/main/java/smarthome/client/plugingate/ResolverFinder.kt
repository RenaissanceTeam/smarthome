package smarthome.client.plugingate

import smarthome.client.domain.api.scripts.resolver.Resolver

class ResolverFinder<IN, OUT>(private val resolvers: List<Resolver<IN, OUT>>): Resolver<IN, OUT> {
    override fun canResolve(item: IN) = false
    
    override fun resolve(item: IN): OUT {
        return resolvers.find { it.canResolve(item) }?.resolve(item)
            ?: throw IllegalStateException("No resolvers found that can resolve $item")
    }
}