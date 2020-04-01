package smarthome.raspberry.scripts.data.controller

import org.springframework.web.bind.annotation.*
import smarthome.raspberry.entity.script.Script
import smarthome.raspberry.scripts.api.data.dto.ScriptDto
import smarthome.raspberry.scripts.api.data.mapper.ScriptDtoMapper
import smarthome.raspberry.scripts.api.data.mapper.resolver.ScriptMapperResolver
import smarthome.raspberry.scripts.api.domain.AddScriptUseCase
import smarthome.raspberry.scripts.api.domain.GetAllScriptsUseCase
import kotlin.reflect.KClass


@RestController
@RequestMapping("/scripts")
class ScriptsController(
        private val addScriptUseCase: AddScriptUseCase,
        private val getAllScriptsUseCase: GetAllScriptsUseCase,
        private val scriptMapperResolver: ScriptMapperResolver
) {

    @PostMapping
    fun add(@RequestBody script: ScriptDto): ScriptDto {
        return script
                .let(resolveMapper(script::class)::mapDto)
                .let(addScriptUseCase::execute)
                .let { resolveMapper(it::class).mapEntity(it) }
    }

    private fun resolveMapper(type: KClass<out Any>) =
            scriptMapperResolver.resolve<ScriptDtoMapper<Script, ScriptDto>>(type)

    @GetMapping
    fun getAll(): List<ScriptDto> {
        return getAllScriptsUseCase.execute()
                .map { resolveMapper(it::class).mapEntity(it) }
    }
}