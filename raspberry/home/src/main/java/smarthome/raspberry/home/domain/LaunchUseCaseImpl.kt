package smarthome.raspberry.home.domain

import smarthome.raspberry.home.api.domain.HomeStateMachine
import smarthome.raspberry.home.api.domain.LaunchUseCase

class LaunchUseCaseImpl(
    private val homeStateMachine: HomeStateMachine
) : LaunchUseCase {

    override fun execute() {
        homeStateMachine.launch()
    }
}