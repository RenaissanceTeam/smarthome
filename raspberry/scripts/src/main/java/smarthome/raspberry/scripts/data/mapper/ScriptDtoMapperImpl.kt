package smarthome.raspberry.scripts.data.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.script.Script
import smarthome.raspberry.scripts.api.data.dto.BlockDto
import smarthome.raspberry.scripts.api.data.dto.ScriptDto
import smarthome.raspberry.scripts.api.data.mapper.BlockDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.DependencyDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.ScriptDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.BlockMapperResolver

@Component
class ScriptDtoMapperImpl(
        private val blockMapperResolver: BlockMapperResolver,
        private val dependencyDtoMapper: DependencyDtoMapper
) : ScriptDtoMapper {

    override fun map(script: Script): ScriptDto {
        return ScriptDto(
                id = script.id,
                name = script.name,
                description = script.description,
                enabled = script.enabled,
                dependencies = script.dependencies.map(dependencyDtoMapper::map),
                blocks = script.blocks.map { blockMapperResolver.resolve<BlockDtoMapper<Block, BlockDto>>(it::class).mapEntity(it) }
        )
    }

    override fun map(dto: ScriptDto): Script {
        val blocks = dto.blocks.map { blockMapperResolver.resolve<BlockDtoMapper<Block, BlockDto>>(it::class).mapDto(it) }
        val dependencies = dto.dependencies.map { dependencyDtoMapper.map(it, blocks) }
        return Script(
                id = dto.id,
                name = dto.name,
                description = dto.description,
                enabled = dto.enabled,
                blocks = blocks,
                dependencies = dependencies
        )
    }
}
