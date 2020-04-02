package smarthome.raspberry.scripts.data.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Action
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.script.Condition
import smarthome.raspberry.entity.script.Dependency
import smarthome.raspberry.scripts.api.data.dto.ActionDto
import smarthome.raspberry.scripts.api.data.dto.ConditionDto
import smarthome.raspberry.scripts.api.data.dto.DependencyDto
import smarthome.raspberry.scripts.api.data.mapper.ActionDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.ConditionDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.DependencyDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.ActionMapperResolver
import smarthome.raspberry.scripts.api.data.mapper.resolver.ConditionMapperResolver
import kotlin.reflect.KClass

@Component
class DependencyDtoMapperImpl(
        private val actionsResolver: ActionMapperResolver,
        private val conditionsResolver: ConditionMapperResolver
) : DependencyDtoMapper {

    override fun map(dto: DependencyDto, blocks: List<Block>): Dependency {
        return Dependency(
                id = dto.id,
                actions = dto.actions.map { resolveActionsMapper(it::class).mapDto(it) },
                conditions = dto.conditions.map { resolveConditionsMapper(it::class).mapDto(it) },
                end = blocks.find { it.id == dto.endBlock }
                        ?: throw IllegalArgumentException("Can't find end block for $dto"),
                start = blocks.find { it.id == dto.startBlock }
                        ?: throw IllegalArgumentException("Can't find start block for $dto")
        )
    }

    private fun resolveActionsMapper(type: KClass<out Any>) =
            actionsResolver.resolve<ActionDtoMapper<Action, ActionDto>>(type)

    private fun resolveConditionsMapper(type: KClass<out Any>) =
            conditionsResolver.resolve<ConditionDtoMapper<Condition, ConditionDto>>(type)

    override fun map(entity: Dependency): DependencyDto {
        return DependencyDto(
                id = entity.id,
                startBlock = entity.start.id,
                endBlock = entity.end.id,
                conditions = entity.conditions.map { resolveConditionsMapper(it::class).mapEntity(it) },
                actions = entity.actions.map { resolveActionsMapper(it::class).mapEntity(it) }
        )
    }
}