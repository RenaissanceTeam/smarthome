package smarthome.client.domain.api.scripts.resolver

interface Resolver<IN, OUT> {
    fun canResolve(item: IN): Boolean
    fun resolve(item: IN): OUT
}