package smarthome.raspberry.scripts.data.controller

import org.springframework.web.bind.annotation.*
import smarthome.raspberry.scripts.api.data.dto.ScriptDto
import smarthome.raspberry.scripts.api.data.mapper.ScriptDtoMapper
import smarthome.raspberry.scripts.api.domain.AddScriptUseCase
import smarthome.raspberry.scripts.api.domain.GetAllScriptsUseCase


@RestController
@RequestMapping("/scripts")
class ScriptsController(
        private val addScriptUseCase: AddScriptUseCase,
        private val getAllScriptsUseCase: GetAllScriptsUseCase,
        private val scriptDtoMapper: ScriptDtoMapper
) {

    @PostMapping
    fun add(@RequestBody script: ScriptDto): ScriptDto {
        return script
                .let(scriptDtoMapper::map)
                .let(addScriptUseCase::execute)
                .let(scriptDtoMapper::map)
    }

    @GetMapping
    fun getAll(): List<ScriptDto> {
        return getAllScriptsUseCase.execute()
                .map(scriptDtoMapper::map)
    }
}