package smarthome.raspberry.scripts.data.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Action
import smarthome.raspberry.entity.script.Condition
import smarthome.raspberry.entity.script.Dependency
import smarthome.raspberry.scripts.api.data.dto.ActionDto
import smarthome.raspberry.scripts.api.data.dto.ConditionDto
import smarthome.raspberry.scripts.api.data.dto.DependencyDto
import smarthome.raspberry.scripts.api.data.mapper.ActionDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.ConditionDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.DependencyDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.DependencyMapperResolver
import kotlin.reflect.KClass

@Component
class DependencyDtoMapperImpl(
        dependencyMapperResolver: DependencyMapperResolver,
        private val actionsResolver: DependencyMapperResolver,
        private val conditionsResolver: DependencyMapperResolver
) : DependencyDtoMapper<Dependency, DependencyDto> {

    init {
        dependencyMapperResolver.register(Dependency::class, DependencyDto::class, this)
    }

    override fun mapDto(dto: DependencyDto): Dependency {
        return Dependency(
                id = dto.id,
                actions = dto.actions.map { resolveActionsMapper(it::class).mapDto(it) },
                conditions = dto.conditions.map { resolveConditionsMapper(it::class).mapDto(it) },
                end = TODO(" add column identifier for block, dependency entity contains only id"),
                start = TODO(" add column identifier for block, dependency entity contains only id")

        )
    }

    private fun resolveActionsMapper(type: KClass<out Any>) =
            actionsResolver.resolve<ActionDtoMapper<Action, ActionDto>>(type)

    private fun resolveConditionsMapper(type: KClass<out Any>) =
            conditionsResolver.resolve<ConditionDtoMapper<Condition, ConditionDto>>(type)

    override fun mapEntity(entity: Dependency): DependencyDto {
        TODO()
    }
}