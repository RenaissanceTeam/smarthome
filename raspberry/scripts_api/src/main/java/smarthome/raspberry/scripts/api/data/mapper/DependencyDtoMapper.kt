package smarthome.raspberry.scripts.api.data.mapper

import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.entity.script.Dependency
import smarthome.raspberry.scripts.api.data.dto.DependencyDto
import smarthome.raspberry.scripts.api.data.dto.ScriptDto

interface DependencyDtoMapper {
    fun map(dto: DependencyDto, blocks: List<Block>): Dependency
    fun map(entity: Dependency): DependencyDto
}