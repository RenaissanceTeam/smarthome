package smarthome.client.data.devices.mapper

import smarthome.client.data.devices.dto.GeneralDeviceAndControllersInfo
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo

class GeneralDeviceAndControllersInfoToGeneralDeviceInfoMapper {
    fun map(allInfo: GeneralDeviceAndControllersInfo) = GeneralDeviceInfo(
        id = allInfo.id,
        name = allInfo.name,
        type = allInfo.type,
        controllers = allInfo.controllers.map { it.id }
    )
}