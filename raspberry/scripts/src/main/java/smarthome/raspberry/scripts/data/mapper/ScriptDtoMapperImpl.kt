package smarthome.raspberry.scripts.data.mapper

import org.springframework.stereotype.Component
import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.script.Script
import smarthome.raspberry.scripts.api.data.dto.BlockDto
import smarthome.raspberry.scripts.api.data.dto.ScriptDto
import smarthome.raspberry.scripts.api.data.mapper.BlockDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.ScriptDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.BlockMapperResolver
import smarthome.raspberry.scripts.api.data.mapper.resolver.ScriptMapperResolver

@Component
class ScriptDtoMapperImpl(
        private val blockMapperResolver: BlockMapperResolver,
        scriptMapperResolver: ScriptMapperResolver
) : ScriptDtoMapper<Script, ScriptDto> {

    init {
        scriptMapperResolver.register(Script::class, ScriptDto::class, this)
    }

    override fun mapDto(dto: ScriptDto): Script {
        return Script(
                id = dto.id,
                name = dto.name,
                description = dto.description,
                enabled = dto.enabled,
                blocks = dto.blocks.map { blockMapperResolver.resolve<BlockDtoMapper<Block, BlockDto>>(it::class).mapDto(it) },
                dependencies = listOf()
        )
    }

    override fun mapEntity(entity: Script): ScriptDto {
        return ScriptDto(
                id = entity.id,
                name = entity.name,
                description = entity.description,
                enabled = entity.enabled,
                dependencies = listOf(),
                blocks = entity.blocks.map { blockMapperResolver.resolve<BlockDtoMapper<Block, BlockDto>>(it::class).mapEntity(it) }
        )
    }

}