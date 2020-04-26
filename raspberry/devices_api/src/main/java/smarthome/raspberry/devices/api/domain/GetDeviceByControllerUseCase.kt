package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.entity.controller.Controller
import smarthome.raspberry.entity.device.Device

interface GetDeviceByControllerUseCase {
    suspend fun execute(controller: Controller): Device
}