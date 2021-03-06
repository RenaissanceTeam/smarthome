package smarthome.client.data.devices

import smarthome.client.data.api.devices.DevicesRepo
import smarthome.client.data.devices.mapper.DeviceDetailsToDeviceMapper
import smarthome.client.data.devices.mapper.GeneralDeviceAndControllersInfoToGeneralDeviceInfoMapper
import smarthome.client.data.retrofit.RetrofitFactory
import smarthome.client.data.util.StringRequestBody
import smarthome.client.domain.api.conrollers.usecases.PipelineControllerToStorageUseCase
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.entity.Device

class DevicesRepoImpl(
        private val retrofitFactory: RetrofitFactory,
        private val detailsToDeviceMapper: DeviceDetailsToDeviceMapper,
        private val deviceAndControllersInfoMapper: GeneralDeviceAndControllersInfoToGeneralDeviceInfoMapper,
        private val pipelineController: PipelineControllerToStorageUseCase
) : DevicesRepo {

    override suspend fun getAdded(): List<GeneralDeviceInfo> {
        return retrofitFactory.createApi(DevicesApi::class.java).getAdded()
                .also { devices ->
                    devices
                            .flatMap { it.controllers }
                            .forEach(pipelineController::execute)
                }
                .map(deviceAndControllersInfoMapper::map)
    }

    override suspend fun getById(deviceId: Long): Device {
        val details = retrofitFactory.createApi(DevicesApi::class.java)
                .getDeviceDetails(deviceId)

        return detailsToDeviceMapper.map(details)
    }

    override suspend fun getPending(): List<GeneralDeviceInfo> {
        return retrofitFactory.createApi(DevicesApi::class.java).getPending()
                .also { devices ->
                    devices
                            .flatMap { it.controllers }
                            .forEach(pipelineController::execute)
                }
                .map(deviceAndControllersInfoMapper::map)
    }

    override suspend fun acceptPending(id: Long) {
        retrofitFactory.createApi(DevicesApi::class.java).accept(id)
    }

    override suspend fun declinePending(id: Long) {
        retrofitFactory.createApi(DevicesApi::class.java).decline(id)
    }

    override suspend fun updateName(deviceId: Long, name: String): Device {
        return retrofitFactory.createApi(DevicesApi::class.java)
                .updateName(deviceId, StringRequestBody((name)))
                .let(detailsToDeviceMapper::map)
    }

    override suspend fun updateDescription(deviceId: Long, description: String): Device {
        return retrofitFactory.createApi(DevicesApi::class.java)
                .updateDescription(deviceId, StringRequestBody(description))
                .let(detailsToDeviceMapper::map)
    }
}