package smarthome.raspberry.controllers.data.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import smarthome.raspberry.controllers.api.data.dto.ControllerDetails
import smarthome.raspberry.controllers.api.data.mapper.ControllerToControllerDetailsMapper
import smarthome.raspberry.controllers.api.domain.GetControllerByIdUseCase
import smarthome.raspberry.util.exceptions.notFound

@RestController
@RequestMapping("api/")
class IotControllersController(
        private val getControllerByIdUseCase: GetControllerByIdUseCase,
        private val controllerDetailsMapper: ControllerToControllerDetailsMapper
) {

    @GetMapping("controllers/{id}")
    fun getDetails(@PathVariable id: Long): ControllerDetails {
        val controller = getControllerByIdUseCase.runCatching { execute(id) }.getOrElse { throw notFound }
        return controllerDetailsMapper.map(controller)
    }
}