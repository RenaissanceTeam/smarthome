package smarthome.raspberry.input

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito
import smarthome.library.common.BaseController
import smarthome.library.common.ControllerServeState
import smarthome.library.common.ControllerState
import smarthome.library.common.IotDevice
import smarthome.raspberry.domain.HomeRepository
import smarthome.raspberry.devices.DevicesUseCase

class InputControllerTest {

    private val repo: HomeRepository = mock()
    private val inputSource: InputControllerDataSource = mock()
    private val devicesUseCase = mock<smarthome.raspberry.devices.DevicesUseCase>()
    private val idleController = createController(1)
    private val idleDevice = createDevice(2, mutableListOf(idleController))

    private val pendingController = createController(1,
            serveState = ControllerServeState.PENDING_READ)
    private val pendingDevice = createDevice(2, mutableListOf(pendingController))
    private val inputController = InputController(devicesUseCase, repo, inputSource)

    @Test
    fun onUserRequest_readController_ProceedReadAndUpdateServeState() {
        Mockito.`when`(repo.getCurrentDevices()).thenAnswer { mutableListOf(idleDevice) }

        runBlocking {
            inputController.onUserRequest(mutableListOf(pendingDevice))

            verify(devicesUseCase).readController(pendingDevice, pendingController)
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
    fun onUserRequest_writeNewState_ProceedWriteAndUpdateServeState() {
        val newState = ControllerState()
        pendingController.state = newState
        pendingController.serveState = ControllerServeState.PENDING_WRITE

        whenever(repo.getCurrentDevices()).then { mutableListOf(idleDevice) }

        runBlocking {
            inputController.onUserRequest(mutableListOf(pendingDevice))

            verify(devicesUseCase)
                    .writeController(pendingDevice, pendingController, newState)
        }
    }
}