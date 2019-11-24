package smarthome.raspberry.controllers.domain

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.library.common.*
import smarthome.raspberry.channel.api.domain.GetChannelForDeviceUseCase
import smarthome.raspberry.controllers.api.domain.ReadControllerUseCase
import smarthome.raspberry.devices.api.domain.DevicesService

class ReadControllerUseCaseImplTest {
    private lateinit var getChannelForDeviceUseCase: GetChannelForDeviceUseCase
    private lateinit var devicesService: DevicesService
    private lateinit var readControllerUseCase: ReadControllerUseCase

    private val pendingController = BaseController(name = "", id = Id("1"))
            .apply { serveState = ControllerServeState.PENDING_READ }
    private val pendingDevice =
            IotDevice(name = "", id = Id("2"), controllers = listOf(pendingController))


    @Before
    fun setup() {
        getChannelForDeviceUseCase = mock {
            onBlocking { execute(any()) }.thenReturn(mock {})
        }
        devicesService = mock {}
        readControllerUseCase =
                ReadControllerUseCaseImpl(getChannelForDeviceUseCase, devicesService)
    }

    @Test
    fun `when pending controller is read its state should be changed to idle`() {

        runBlocking {
            readControllerUseCase.execute(pendingDevice, pendingController)

            assertThat(pendingController.serveState).isEqualTo(ControllerServeState.IDLE)
        }
    }

    @Test
    fun `when read controller should find channel with usecase`() {
        runBlocking {
            readControllerUseCase.execute(pendingDevice, pendingController)
            verify(getChannelForDeviceUseCase).execute(pendingDevice)
        }
    }
    
    @Test
    fun `when controller is read new state should be assigned to it`() {
        runBlocking {
            val newState = ControllerState()
            val channel: DeviceChannel = mock {
                onBlocking { read(pendingDevice, pendingController) }.thenReturn(newState)
            }
            whenever(getChannelForDeviceUseCase.execute(pendingDevice)).thenReturn(channel)

            readControllerUseCase.execute(pendingDevice, pendingController)

            assertThat(pendingController.state).isEqualTo(newState)
        }
    }

    @Test
    fun `when controller is read it's device should be saved`() {
        runBlocking {
            readControllerUseCase.execute(pendingDevice, pendingController)
            verify(devicesService).saveDevice(pendingDevice)
        }
    }

}