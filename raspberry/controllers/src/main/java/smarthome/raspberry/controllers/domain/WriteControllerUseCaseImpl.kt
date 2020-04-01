package smarthome.raspberry.controllers.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.channel.api.domain.GetChannelForDeviceUseCase
import smarthome.raspberry.controllers.api.domain.WriteControllerUseCase
import smarthome.raspberry.controllers.data.ControllersRepo
import smarthome.raspberry.entity.controller.Controller

@Component
open class WriteControllerUseCaseImpl(
        private val getChannelForDeviceUseCase: GetChannelForDeviceUseCase,
        private val repo: ControllersRepo

) : WriteControllerUseCase {
    override fun execute(controller: Controller, state: String): String {
        val channel = getChannelForDeviceUseCase.execute(controller.device)
        val newState = channel.write(controller, state)

        repo.save(controller.copy(state = newState))
        return newState
    }
}