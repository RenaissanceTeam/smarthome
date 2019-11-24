package smarthome.raspberry.devices.data.storage

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.verify
import smarthome.library.common.IotDevice
import smarthome.raspberry.devices.api.domain.DevicesService
import smarthome.raspberry.devices.data.DevicesRepository
import smarthome.raspberry.devices.domain.DevicesServiceImpl

class DevicesServiceImplTest {

    private lateinit var repo: DevicesRepository
    private lateinit var device: IotDevice
    private lateinit var devicesService: DevicesService


    @Before
    fun setUp() {
        repo = mock {}
        device = mock {}
        devicesService = DevicesServiceImpl(repo)
    }

    @Test
    fun homeWithoutDevices_addNewDevice_AddDeviceAsPending() {
        runBlocking {
            whenever(repo.getCurrentDevices()).thenReturn(listOf())
            devicesService.addNewDevice(device)

            verify(repo).addPendingDevice(device)
        }
    }

    @Test
    fun hasPendingDevice_acceptPendingDevice_RemoveFromPendingAndAddDevice() {
        runBlocking {
            devicesService.acceptPendingDevice(device)

            verify(repo).removePendingDevice(device)
            verify(repo).addDevice(device)
        }
    }
}