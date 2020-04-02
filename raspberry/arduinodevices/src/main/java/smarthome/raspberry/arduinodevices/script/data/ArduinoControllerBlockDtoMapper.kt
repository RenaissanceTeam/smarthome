package smarthome.raspberry.arduinodevices.script.data

import org.springframework.stereotype.Component
import smarthome.raspberry.arduinodevices.script.data.ArduinoControllerBlockDto
import smarthome.raspberry.arduinodevices.script.domain.entity.ArduinoControllerBlock
import smarthome.raspberry.controllers.api.domain.GetControllerByIdUseCase
import smarthome.raspberry.scripts.api.data.mapper.BlockDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.BlockMapperResolver

@Component
class ArduinoControllerBlockDtoMapper(
        private val getControllerByIdUseCase: GetControllerByIdUseCase,
        blockMapperResolver: BlockMapperResolver
) : BlockDtoMapper<ArduinoControllerBlock, ArduinoControllerBlockDto> {

    init {
        blockMapperResolver.register(ArduinoControllerBlock::class, ArduinoControllerBlockDto::class, this)
    }

    override fun mapDto(dto: ArduinoControllerBlockDto): ArduinoControllerBlock {

        return ArduinoControllerBlock(
                id = dto.id,
                position = dto.position,
                controller = getControllerByIdUseCase.execute(dto.controllerId)
        )
    }

    override fun mapEntity(entity: ArduinoControllerBlock): ArduinoControllerBlockDto {
        return ArduinoControllerBlockDto(
                id = entity.id,
                position = entity.position,
                controllerId = entity.controller.id
        )
    }
}