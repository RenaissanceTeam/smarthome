package smarthome.raspberry.devices.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.library.common.IotDevice
import smarthome.raspberry.devices.api.domain.AcceptPendingDeviceUseCase
import smarthome.raspberry.devices.data.DevicesRepository

class AcceptPendingDeviceUseCaseImplTest {
    private lateinit var repo: DevicesRepository
    private lateinit var device: IotDevice
    private lateinit var useCase: AcceptPendingDeviceUseCase

    @Before
    fun setUp() {
        repo = mock {}
        device = mock {}
        useCase = AcceptPendingDeviceUseCaseImpl(repo)
    }

    @Test
    fun `when execute should remove pending device from repo and add device`() {
        runBlocking {
            useCase.execute(device)

            verify(repo).removePendingDevice(device)
            verify(repo).addDevice(device)
        }
    }
}