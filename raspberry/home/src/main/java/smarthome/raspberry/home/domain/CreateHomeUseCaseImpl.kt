package smarthome.raspberry.home.domain

import smarthome.raspberry.home.api.domain.CreateHomeUseCase
import smarthome.raspberry.home.api.domain.GenerateUniqueHomeIdUseCase
import smarthome.raspberry.home.api.domain.eventbus.PublishEventUseCase
import smarthome.raspberry.home.api.domain.eventbus.events.HasHomeIdentifier
import smarthome.raspberry.home.data.HomeRepository

class CreateHomeUseCaseImpl(
    private val generateUniqueHomeIdUseCase: GenerateUniqueHomeIdUseCase,
    private val repository: HomeRepository,
    private val publishEventUseCase: PublishEventUseCase
) : CreateHomeUseCase {
    
    override suspend fun execute() {
        val homeId = generateUniqueHomeIdUseCase.execute()
        repository.saveHome(homeId)
        publishEventUseCase.execute(HasHomeIdentifier())
    }
}