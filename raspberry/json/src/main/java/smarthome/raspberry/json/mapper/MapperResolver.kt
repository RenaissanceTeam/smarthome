package smarthome.raspberry.json.mapper

import kotlin.reflect.KClass


abstract class MapperResolver<ENTITY : Any, DTO : Any, MAPPER : DtoMapper<*, *>> {

    private val mappers = mutableMapOf<KClass<out Any>, MAPPER>()

    fun resolveFromType(type: KClass<out Any>): MAPPER {
        return mappers[type] ?: throw IllegalStateException("No mapper for $type")
    }

    fun register(entity: KClass<out ENTITY>, dto: KClass<out DTO>, mapper: MAPPER) {
        mappers[entity] = mapper
        mappers[dto] = mapper
    }

    inline fun <reified M> resolve(type: KClass<out Any>): M {
        return resolveFromType(type) as M
    }
}
