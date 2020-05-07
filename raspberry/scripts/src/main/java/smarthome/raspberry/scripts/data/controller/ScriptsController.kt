package smarthome.raspberry.scripts.data.controller

import org.springframework.web.bind.annotation.*
import smarthome.raspberry.scripts.api.data.dto.ScriptDto
import smarthome.raspberry.scripts.api.data.dto.ScriptItemDto
import smarthome.raspberry.scripts.api.data.mapper.ScriptDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.ScriptItemDtoMapper
import smarthome.raspberry.scripts.api.domain.usecase.SaveScriptUseCase
import smarthome.raspberry.scripts.api.domain.usecase.GetAllScriptsUseCase
import smarthome.raspberry.scripts.api.domain.usecase.GetScriptByIdUseCase


@RestController
@RequestMapping("api/scripts")
class ScriptsController(
        private val saveScriptUseCase: SaveScriptUseCase,
        private val getAllScriptsUseCase: GetAllScriptsUseCase,
        private val scriptDtoMapper: ScriptDtoMapper,
        private val getScriptById: GetScriptByIdUseCase,
        private val scriptItemDtoMapper: ScriptItemDtoMapper
) {

    @PostMapping
    fun add(@RequestBody script: ScriptDto): ScriptDto {
        return script
                .let(scriptDtoMapper::map)
                .let(saveScriptUseCase::execute)
                .let(scriptDtoMapper::map)
    }

    @GetMapping
    fun getAll(): List<ScriptItemDto> {
        return getAllScriptsUseCase.execute()
                .map(scriptItemDtoMapper::map)
    }

    @GetMapping("/{id}")
    fun getDetails(@PathVariable id: Long): ScriptDto {
        return getScriptById.execute(id)
                .let(scriptDtoMapper::map)
    }
}