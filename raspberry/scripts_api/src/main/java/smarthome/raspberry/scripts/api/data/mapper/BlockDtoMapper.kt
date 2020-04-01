package smarthome.raspberry.scripts.api.data.mapper

import smarthome.raspberry.entity.script.Block
import smarthome.raspberry.json.mapper.DtoMapper
import smarthome.raspberry.scripts.api.data.dto.BlockDto

interface BlockDtoMapper<E : Block, D : BlockDto> : DtoMapper<E, D>

