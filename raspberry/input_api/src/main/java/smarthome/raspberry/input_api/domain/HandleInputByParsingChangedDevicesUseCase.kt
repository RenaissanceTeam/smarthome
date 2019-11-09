package smarthome.raspberry.input_api.domain

import smarthome.library.common.IotDevice

interface HandleInputByParsingChangedDevicesUseCase {
    suspend fun execute(changedDevices: MutableList<IotDevice>)
}