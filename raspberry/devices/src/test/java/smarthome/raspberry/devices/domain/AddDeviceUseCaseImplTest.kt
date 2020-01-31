package smarthome.raspberry.devices.domain

import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.devices.api.domain.AddDeviceUseCase
import smarthome.raspberry.devices.api.domain.dto.DeviceDTO
import smarthome.raspberry.devices.data.DeviceStatusRepository
import smarthome.raspberry.devices.data.DevicesRepository
import smarthome.raspberry.devices.domain.mapper.DeviceDtoToDeviceMapper
import smarthome.raspberry.entity.Device
import smarthome.raspberry.entity.DeviceStatus
import smarthome.raspberry.entity.DeviceStatuses

class AddDeviceUseCaseImplTest {
    private lateinit var devicesRepo: DevicesRepository
    private lateinit var statusRepo: DeviceStatusRepository
    private lateinit var mapper: DeviceDtoToDeviceMapper
    private lateinit var addDeviceUseCase: AddDeviceUseCase

    @Before
    fun setUp() {
        devicesRepo = mock {}
        statusRepo = mock {}
        mapper = mock {}
        addDeviceUseCase = AddDeviceUseCaseImpl(devicesRepo, mapper, statusRepo)
    }

    @Test
    fun `when add new device should add pending device to repo`() {
        val device = mock<DeviceDTO> {}
        val deviceEntity = mock<Device>()
        whenever(mapper.map(device)).then { deviceEntity }
        addDeviceUseCase.execute(device)
    
        verify(devicesRepo).save(deviceEntity)
        verify(statusRepo).save<DeviceStatus>(
            argThat { this.status == DeviceStatuses.PENDING.name })
    }
}