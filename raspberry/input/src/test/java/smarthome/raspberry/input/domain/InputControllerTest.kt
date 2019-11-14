package smarthome.raspberry.input.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.library.common.BaseController
import smarthome.library.common.ControllerServeState
import smarthome.library.common.ControllerState
import smarthome.library.common.IotDevice
import smarthome.raspberry.controllers.api.domain.ReadControllerUseCase
import smarthome.raspberry.controllers.api.domain.WriteControllerUseCase
import smarthome.raspberry.devices.api.domain.AcceptPendingDeviceUseCase
import smarthome.raspberry.devices.api.domain.AddDeviceUseCase
import smarthome.raspberry.devices.api.domain.GetDevicesUseCase
import smarthome.raspberry.devices.api.domain.RemoveDeviceUseCase
import smarthome.raspberry.input.api.domain.HandleInputByParsingChangedDevicesUseCase

class HandleInputByParsingChangedDevicesUseCaseImplTest {

    private lateinit var getDevicesUseCase: GetDevicesUseCase
    private lateinit var addDeviceUseCase: AddDeviceUseCase
    private lateinit var removeDeviceUseCase: RemoveDeviceUseCase
    private lateinit var acceptPendingDeviceUseCase: AcceptPendingDeviceUseCase
    private lateinit var readControllerUseCase: ReadControllerUseCase
    private lateinit var writeControllerUseCase: WriteControllerUseCase
    private lateinit var useCase: HandleInputByParsingChangedDevicesUseCase

    private val pendingReadController = createController(1,
                                                         serveState = ControllerServeState.PENDING_READ)
    private val pendingReadDevice = createDevice(2, mutableListOf(pendingReadController))
    private val idleController = createController(1)
    private val idleDevice = createDevice(2, mutableListOf(idleController))

    @Before
    fun setup() {
        getDevicesUseCase = mock {}
        addDeviceUseCase = mock {}
        removeDeviceUseCase = mock {}
        acceptPendingDeviceUseCase = mock {}
        readControllerUseCase = mock {}
        writeControllerUseCase = mock {}
        useCase = HandleInputByParsingChangedDevicesUseCaseImpl(getDevicesUseCase,
                                                                addDeviceUseCase,
                                                                acceptPendingDeviceUseCase,
                                                                readControllerUseCase,
                                                                writeControllerUseCase,
                                                                removeDeviceUseCase)
    }

    @Test
    fun `when user request is read controller should call read controller use case`() {
        runBlocking {
            whenever(getDevicesUseCase.execute()).thenAnswer { mutableListOf(idleDevice) }

            useCase.execute(mutableListOf(pendingReadDevice))

            verify(readControllerUseCase).execute(pendingReadDevice, pendingReadController)
        }
    }

    private fun createController(guid: Long,
                                 name: String = "",
                                 state: ControllerState? = null,
                                 serveState: ControllerServeState = ControllerServeState.IDLE)
            : BaseController {
        return BaseController(name,
                              serveState = serveState,
                              guid = guid,
                              state = state)
    }

    private fun createDevice(guid: Long,
                             controllers: MutableList<BaseController>,
                             name: String = "", description:
                             String = ""): IotDevice {
        return IotDevice(name,
                         description,
                         controllers = controllers,
                         guid = guid
        )

    }

    @Test
    fun `when controller is pending write should call controller write use case`() {
        runBlocking {

            val newState = ControllerState()
            val pendingToWriteController =
                    createController(1, serveState = ControllerServeState.PENDING_WRITE)
            pendingToWriteController.state = newState
            val pendingToWriteDevice = createDevice(2, mutableListOf(pendingToWriteController))

            whenever(getDevicesUseCase.execute()).thenReturn(mutableListOf(idleDevice))
            useCase.execute(mutableListOf(pendingToWriteDevice))

            verify(writeControllerUseCase)
                    .execute(pendingToWriteDevice, pendingToWriteController, newState)
        }
    }

}