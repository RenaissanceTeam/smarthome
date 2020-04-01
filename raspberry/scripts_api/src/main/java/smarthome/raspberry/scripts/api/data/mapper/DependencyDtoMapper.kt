package smarthome.raspberry.scripts.api.data.mapper

import smarthome.raspberry.entity.script.Dependency
import smarthome.raspberry.json.mapper.DtoMapper
import smarthome.raspberry.scripts.api.data.dto.DependencyDto

interface DependencyDtoMapper<E : Dependency, D : DependencyDto> : DtoMapper<E, D>