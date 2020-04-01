package smarthome.raspberry.scripts.api.data.dto

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver
import smarthome.raspberry.entity.ID_NOT_DEFINED
import smarthome.raspberry.json.InheritanceTypeIdResolver
import smarthome.raspberry.json.classType

@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.PROPERTY, property = classType)
@JsonTypeIdResolver(InheritanceTypeIdResolver::class)
open class ActionDto(
        open val id: Long = ID_NOT_DEFINED
)