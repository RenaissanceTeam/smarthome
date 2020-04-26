package smarthome.raspberry.scripts.api.data.mapper.resolver

import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.json.mapper.MapperResolver
import smarthome.raspberry.scripts.api.data.dto.BlockDto
import smarthome.raspberry.scripts.api.data.mapper.BlockDtoMapper

abstract class BlockMapperResolver : MapperResolver<Block, BlockDto, BlockDtoMapper<out Block, out BlockDto>>()
