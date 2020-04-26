package smarthome.raspberry.controllers.data.controller

import org.springframework.web.bind.annotation.*
import smarthome.raspberry.controllers.api.data.dto.ControllerDetails
import smarthome.raspberry.controllers.api.data.mapper.ControllerToControllerDetailsMapper
import smarthome.raspberry.controllers.api.domain.GetControllerByIdUseCase
import smarthome.raspberry.controllers.api.domain.ReadControllerUseCase
import smarthome.raspberry.controllers.api.domain.WriteControllerUseCase
import smarthome.raspberry.controllers.data.StateDto
import smarthome.raspberry.entity.controller.Controller
import smarthome.raspberry.util.exceptions.notFound

@RestController
@RequestMapping("api/")
class IotControllersController(
        private val getControllerByIdUseCase: GetControllerByIdUseCase,
        private val readControllerUseCase: ReadControllerUseCase,
        private val writeControllerUseCase: WriteControllerUseCase,
        private val controllerDetailsMapper: ControllerToControllerDetailsMapper
) {

    @GetMapping("controllers/{id}")
    fun getDetails(@PathVariable id: Long): ControllerDetails {
        val controller = getController(id)
        return controllerDetailsMapper.map(controller)
    }

    @GetMapping("controllers/{id}/read")
    fun readState(@PathVariable id: Long): StateDto {
        val newState = readControllerUseCase.execute(getController(id))
        return StateDto(newState)
    }

    @PostMapping("controllers/{id}/write")
    fun writeState(@PathVariable id: Long, @RequestBody state: StateDto): StateDto {
        val newState = writeControllerUseCase.execute(getController(id), state.state)
        return StateDto(newState)
    }

    private fun getController(id: Long): Controller {
        return getControllerByIdUseCase.runCatching { execute(id) }.getOrElse { throw notFound }
    }
}