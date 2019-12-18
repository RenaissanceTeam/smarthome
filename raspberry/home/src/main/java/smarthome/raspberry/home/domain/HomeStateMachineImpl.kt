package smarthome.raspberry.home.domain

import smarthome.raspberry.home.api.domain.HomeStateMachine
import smarthome.raspberry.home.api.domain.eventbus.events.*
import smarthome.raspberry.home.data.EventBusRepository

class HomeStateMachineImpl(
    private val eventBusRepository: EventBusRepository
) : HomeStateMachine {
    // todo refactor with https://github.com/Tinder/StateMachine, for now just dumb implementation
    
    
    private var hasUser = false
    private var hasHomeId = false
    
    init {
        eventBusRepository.addEvent(NeedUser())
        eventBusRepository.addEvent(Paused())
    }
    
    
    override fun registerEvent(event: Event) {
        when (event) {
            is HasUser -> {
                hasUser = true
                
                if (!hasHomeId) eventBusRepository.addEvent(NeedHomeIdentifier())
            }
            is HasHomeIdentifier -> {
                hasHomeId = true
                
                if (hasUser && hasHomeId) eventBusRepository.addEvent(Resumed())
            }
        }
    }
}