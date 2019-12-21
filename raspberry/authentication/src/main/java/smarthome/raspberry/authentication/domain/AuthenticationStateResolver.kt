package smarthome.raspberry.authentication.domain

import smarthome.raspberry.authentication.api.flow.SignInFlowLauncher
import smarthome.raspberry.authentication.data.AuthRepo
import smarthome.raspberry.home.api.domain.eventbus.ObserveHomeEventsUseCase
import smarthome.raspberry.home.api.domain.eventbus.PublishEventUseCase
import smarthome.raspberry.home.api.domain.eventbus.events.HasUser
import smarthome.raspberry.home.api.domain.eventbus.events.NeedUser

class AuthenticationStateResolver(
    private val repo: AuthRepo,
    private val publishEventUseCase: PublishEventUseCase,
    private val signInFlowLauncher: SignInFlowLauncher,
    getHomeEventsUseCase: ObserveHomeEventsUseCase
) {
    init {
        getHomeEventsUseCase.execute().subscribe {
            when (it) {
                is NeedUser -> handleNeedUser()
            }
        }
    }
    
    private fun handleNeedUser() {
        when (repo.hasUser()) {
            true -> publishEventUseCase.execute(HasUser())
            false -> signInFlowLauncher.launch()
        }
    }
}