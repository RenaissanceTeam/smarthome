package smarthome.raspberry.devices.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.library.common.IotDevice
import smarthome.raspberry.devices.api.domain.AddDeviceUseCase
import smarthome.raspberry.devices.data.DevicesRepository

class AddDeviceUseCaseImplTest {
    private lateinit var repo: DevicesRepository
    private lateinit var device: IotDevice
    private lateinit var addDeviceUseCase: AddDeviceUseCase

    @Before
    fun setUp() {
        repo = mock {}
        device = mock {}
        addDeviceUseCase = AddDeviceUseCaseImpl(repo)
    }

    @Test
    fun `when add new device should add pending device to repo`() {
        runBlocking {
            whenever(repo.getCurrentDevices()).thenReturn(listOf())
            addDeviceUseCase.execute(device)

            verify(repo).addPendingDevice(device)
        }
    }
}