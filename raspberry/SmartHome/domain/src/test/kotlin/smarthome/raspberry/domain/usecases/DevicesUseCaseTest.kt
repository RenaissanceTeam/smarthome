package smarthome.raspberry.domain.usecases

import com.nhaarman.mockitokotlin2.argThat
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import smarthome.library.common.BaseController
import smarthome.library.common.ControllerServeState
import smarthome.library.common.ControllerState
import smarthome.library.common.IotDevice
import smarthome.raspberry.domain.HomeRepository

class DevicesUseCaseTest {

    @Mock
    private lateinit var repo: HomeRepository
    @Mock
    private lateinit var device: IotDevice

    private val devicesUseCase by lazy { DevicesUseCase(repo) }
    private val idleController = createController(1)
    private val idleDevice = createDevice(2, mutableListOf(idleController))

    private val pendingController = createController(1,
            serveState = ControllerServeState.PENDING_READ)
    private val pendingDevice = createDevice(2, mutableListOf(pendingController))


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun homeWithoutDevices_addNewDevice_AddDeviceAsPending() {
        runBlocking {
            devicesUseCase.addNewDevice(device)

            verify(repo).addPendingDevice(device)
        }
    }

    @Test
    fun hasPendingDevice_acceptPendingDevice_RemoveFromPendingAndAddDevice() {
        runBlocking {
            devicesUseCase.acceptPendingDevice(device)

            verify(repo).removePendingDevice(device)
            verify(repo).addDevice(device)
        }
    }

    @Test
    fun onUserRequest_readController_ProceedReadAndUpdateServeState() {
        `when`(repo.getCurrentDevices()).thenAnswer { mutableListOf(idleDevice) }

        runBlocking {
            devicesUseCase.onUserRequest(mutableListOf(pendingDevice))

            verify(repo).proceedReadController(pendingController)
            verify(repo).saveDevice(pendingDevice)
            assertThat(pendingController.serveState, `is`(ControllerServeState.IDLE))
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

        `when`(repo.getCurrentDevices()).thenAnswer { mutableListOf(idleDevice) }

        runBlocking {
            devicesUseCase.onUserRequest(mutableListOf(pendingDevice))

            verify(repo).proceedWriteController(pendingController, newState)
            verify(repo).saveDevice(pendingDevice)
            assertThat(pendingController.serveState, IsEqual(ControllerServeState.IDLE))
        }
    }

    @Test
    fun onUserRequest_readController_savesNewReadValue() {
        `when`(repo.getCurrentDevices()).thenAnswer { mutableListOf(idleDevice) }
        val stateBefore = ControllerState()
        val stateAfter = ControllerState()

        pendingController.state = stateBefore

        runBlocking {
            `when`(repo.proceedReadController(pendingController)).thenAnswer {
                pendingController.state = stateAfter
                return@thenAnswer pendingController
            }

            devicesUseCase.onUserRequest(mutableListOf(pendingDevice))
            verify(repo).saveDevice(argThat {
                controllers.first().state == stateAfter
            })
        }
    }
}