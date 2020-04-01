package smarthome.raspberry.scripts.api.data.mapper.resolver

import smarthome.raspberry.entity.script.Dependency
import smarthome.raspberry.json.mapper.MapperResolver
import smarthome.raspberry.scripts.api.data.dto.DependencyDto
import smarthome.raspberry.scripts.api.data.mapper.DependencyDtoMapper

abstract class DependencyMapperResolver : MapperResolver<Dependency, DependencyDto, DependencyDtoMapper<out Dependency, out DependencyDto>>()