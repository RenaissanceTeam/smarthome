package smarthome.raspberry.controllers.domain

import org.springframework.stereotype.Component
import smarthome.raspberry.channel.api.domain.GetChannelForDeviceUseCase
import smarthome.raspberry.controllers.api.domain.ReadControllerUseCase
import smarthome.raspberry.controllers.data.ControllersRepo
import smarthome.raspberry.devices.api.domain.SaveDeviceUseCase
import smarthome.raspberry.entity.Controller

@Component
open class ReadControllerUseCaseImpl(
        private val getChannelForDeviceUseCase: GetChannelForDeviceUseCase,
        private val controllersRepo: ControllersRepo) :
        ReadControllerUseCase {

    override fun execute(controller: Controller): String {
        val channel = getChannelForDeviceUseCase.execute(controller.device)
        val newState = channel.read(controller)

        controllersRepo.save(controller.copy(state = newState))
        return newState
    }
}