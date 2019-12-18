package smarthome.raspberry.home.domain

import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.runBlocking
import smarthome.raspberry.authentication.api.flow.SignInFlowLauncher
import smarthome.raspberry.home.api.domain.CreateHomeUseCase
import smarthome.raspberry.home.api.domain.HomeStateMachine
import smarthome.raspberry.home.api.domain.eventbus.events.*
import smarthome.raspberry.home.data.EventBusRepository

class HomeStateMachineImpl(
    private val eventBusRepository: EventBusRepository,
    private val signInFlowLauncher: SignInFlowLauncher,
    private val createHomeUseCase: CreateHomeUseCase
) : HomeStateMachine {
    // todo refactor with https://github.com/Tinder/StateMachine, for now just dumb implementation
    
    private var hasUser = false
    private var hasHomeId = false
    
    override fun launch() {
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
    
    private val stateResolver = eventBusRepository.getEvents()
        .observeOn(Schedulers.io())
        .subscribeBy {
            when (it) {
                is NeedUser -> {
                    signInFlowLauncher.launch()
                }
                is NeedHomeIdentifier -> runBlocking { createHomeUseCase.execute() }
            }
        }
}