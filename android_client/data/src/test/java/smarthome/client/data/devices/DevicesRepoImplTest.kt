package smarthome.client.data.devices

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.client.data.api.devices.DevicesRepo
import smarthome.client.data.devices.dto.GeneralDeviceAndControllersInfo
import smarthome.client.data.devices.mapper.DeviceDetailsToDeviceMapper
import smarthome.client.data.devices.mapper.GeneralDeviceAndControllersInfoToGeneralDeviceInfoMapper
import smarthome.client.data.retrofit.RetrofitFactory
import smarthome.client.domain.api.conrollers.usecases.PipelineControllerToStorageUseCase
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
import smarthome.client.entity.Controller

class DevicesRepoImplTest {
    
    private lateinit var repo: DevicesRepo
    private lateinit var retrofitFactory: RetrofitFactory
    private lateinit var detailsToDeviceMapper: DeviceDetailsToDeviceMapper
    private lateinit var deviceMapper: GeneralDeviceAndControllersInfoToGeneralDeviceInfoMapper
    private lateinit var pipelineController: PipelineControllerToStorageUseCase
    private lateinit var api: DevicesApi
    
    @Before
    fun setUp() {
        api = mock { }
        retrofitFactory = mock { on { createApi(DevicesApi::class.java) }.then { api } }
        detailsToDeviceMapper = mock {}
        deviceMapper = mock {}
        pipelineController = mock {}
        repo = DevicesRepoImpl(retrofitFactory, detailsToDeviceMapper, deviceMapper,
            pipelineController)
    }
    
    @Test
    fun `when receive device and controllers info should pipeline controllers to storage`() {
        val controller = mock<Controller>()
        val deviceAndControllers = mock<GeneralDeviceAndControllersInfo> {
            on { controllers }.then { listOf(controller) }
        }
        
        runBlocking { whenever(api.getAdded()).then { listOf(deviceAndControllers) } }
        whenever(deviceMapper.map(deviceAndControllers)).then { mock<GeneralDeviceInfo> {} }
        
        runBlocking { repo.getAdded() }
        verify(pipelineController).execute(controller)
    }
}