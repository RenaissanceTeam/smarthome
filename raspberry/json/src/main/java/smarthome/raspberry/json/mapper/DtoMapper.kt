package smarthome.raspberry.json.mapper

interface DtoMapper<ENTITY, DTO> {
    fun mapDto(dto: DTO): ENTITY
    fun mapEntity(entity: ENTITY): DTO
}
