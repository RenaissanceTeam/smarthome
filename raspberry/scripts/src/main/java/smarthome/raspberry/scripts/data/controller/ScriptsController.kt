package smarthome.raspberry.scripts.data.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import smarthome.raspberry.scripts.api.data.dto.ScriptDto
import smarthome.raspberry.scripts.api.data.mapper.resolver.ScriptMapperResolver
import smarthome.raspberry.scripts.api.domain.AddScriptUseCase


@RestController
@RequestMapping("/scripts")
class ScriptsController(
        private val addScriptUseCase: AddScriptUseCase,
        private val scriptMapperResolver: ScriptMapperResolver
) {

    @PostMapping("")
    fun add(@RequestBody script: ScriptDto): ScriptDto {
        val mapper = scriptMapperResolver.resolveFromDto(script)

        return script
                .let(mapper::mapDto)
                .let(addScriptUseCase::execute)
                .let(mapper::mapEntity)
    }
}