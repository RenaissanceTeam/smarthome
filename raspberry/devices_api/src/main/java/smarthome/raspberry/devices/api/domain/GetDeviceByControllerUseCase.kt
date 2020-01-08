package smarthome.raspberry.devices.api.domain

import smarthome.raspberry.entity.Controller
import smarthome.raspberry.entity.Device

interface GetDeviceByControllerUseCase {
    suspend fun execute(controller: Controller): Device
}