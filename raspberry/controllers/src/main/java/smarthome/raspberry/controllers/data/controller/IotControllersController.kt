package smarthome.raspberry.controllers.data.controller

import org.springframework.web.bind.annotation.*
import smarthome.raspberry.controllers.api.data.dto.ControllerDetails
import smarthome.raspberry.controllers.api.data.mapper.ControllerToControllerDetailsMapper
import smarthome.raspberry.controllers.api.domain.GetControllerByIdUseCase
import smarthome.raspberry.controllers.api.domain.ReadControllerUseCase
import smarthome.raspberry.entity.Controller
import smarthome.raspberry.util.exceptions.notFound

@RestController
@RequestMapping("api/")
class IotControllersController(
        private val getControllerByIdUseCase: GetControllerByIdUseCase,
        private val readControllerUseCase: ReadControllerUseCase,
        private val controllerDetailsMapper: ControllerToControllerDetailsMapper
) {

    @GetMapping("controllers/{id}")
    fun getDetails(@PathVariable id: Long): ControllerDetails {
        val controller = getController(id)
        return controllerDetailsMapper.map(controller)
    }

    @PostMapping("controllers/{id}")
    fun readState(@PathVariable id: Long): String {
        return readControllerUseCase.execute(getController(id))
    }

    private fun getController(id: Long): Controller {
        return getControllerByIdUseCase.runCatching { execute(id) }.getOrElse { throw notFound }
    }
}