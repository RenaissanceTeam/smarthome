package smarthome.raspberry.data

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.verify
import smarthome.library.common.*
import smarthome.raspberry.authentication_api.AuthRepo
import smarthome.raspberry.domain.HomeRepository
import smarthome.raspberry.domain.NoControllerException
import smarthome.raspberry.domain.NoDeviceException
import smarthome.raspberry.controllers.ControllersUseCase
import smarthome.raspberry.devices.DevicesUseCase
import smarthome.raspberry.home.HomeUseCase

abstract class A(name: String, description: String?,
                 serveState: DeviceServeState, guid: Long,
                 controllers: List<BaseController>)
    : IotDevice(name, description, serveState, guid, controllers)

abstract class B(name: String, description: String?,
                 serveState: DeviceServeState, guid: Long,
                 controllers: List<BaseController>)
    : IotDevice(name, description, serveState, guid, controllers)


class HomeRepositoryImplTest {

    private lateinit var repo: HomeRepository
    private val localStorage: LocalStorage = mock()
    private val remoteStorage: RemoteStorage = mock()
    private val deviceChannel_A: DeviceChannel = mock()
    private val deviceChannel_B: DeviceChannel = mock()
    private val device_A: A = mock()
    private val device_B: B = mock()
    private val controller: BaseController = mock()
    private val devicesUseCase: smarthome.raspberry.devices.DevicesUseCase = mock()
    private val homeUseCase: smarthome.raspberry.home.HomeUseCase = mock()
    private val controllersUseCase: smarthome.raspberry.controllers.ControllersUseCase = mock()
    private val authRepo: smarthome.raspberry.authentication_api.AuthRepo = mock()


    @Test
    fun hasController_proceedReadController_ReadStateFromChannel() {
        runBlocking {
            repo = createRepository(mapOf(
                    device_A.javaClass.simpleName to deviceChannel_A
            ))

            whenever(localStorage.findDevice(controller)).then { device_A }
            repo.proceedReadController(controller)
            verify(deviceChannel_A).read(device_A, controller)
        }
    }

    private fun createRepository(
            devices: Map<String, DeviceChannel>): HomeRepositoryImpl {
        return HomeRepositoryImpl(
                { a, b -> localStorage }, { devicesUseCase }, { homeUseCase },
                { controllersUseCase },
                { remoteStorage }, devices.mapValues { { _: DeviceChannelOutput -> it.value } },
                authRepo
        )
    }

    @Test
    fun hasTwoChannels_proceedReadController_ReadStateFromProperChannel() {
        runBlocking {
            repo = createRepository(mapOf(
                    device_A.javaClass.simpleName to deviceChannel_A,
                    device_B.javaClass.simpleName to deviceChannel_B
            ))

            whenever(localStorage.findDevice(any())).then { device_A }
            repo.proceedReadController(controller)
            verify(deviceChannel_A).read(device_A, controller)

            whenever(localStorage.findDevice(any())).then { device_B }
            repo.proceedReadController(controller)
            verify(deviceChannel_B).read(device_B, controller)
        }
    }

    @Test
    fun hasTwoChannels_proceedWrite_WriteStateToProperChannel() {
        runBlocking {
            repo = createRepository(mapOf(
                    device_A.javaClass.simpleName to deviceChannel_A,
                    device_B.javaClass.simpleName to deviceChannel_B
            ))

            val state = ControllerState()

            whenever(localStorage.findDevice(any())).then { device_A }
            repo.proceedWriteController(controller, state)
            verify(deviceChannel_A).writeState(device_A, controller, state)
        }
    }

    @Test
    fun noDevices_findController_shouldThrow() {
        runBlocking {
            val repo: DeviceChannelOutput = createRepository(mapOf(
                    device_A.javaClass.simpleName to deviceChannel_A
            ))

            whenever(localStorage.getDevices()).thenReturn(mutableListOf())

            shouldThrow<NoControllerException> {
                repo.findController(123)
            }
        }
    }

    @Test
    fun hasDevicesWithProperController_findController_shouldReturnController() {
        runBlocking {
            val repo: DeviceChannelOutput = createRepository(mapOf(
                    device_A.javaClass.simpleName to deviceChannel_A
            ))

            val guid = 123L
            val notProperController = mock<BaseController>()
            whenever(device_A.controllers).then { listOf(controller) }
            whenever(device_B.controllers).then { listOf(notProperController) }

            whenever(controller.guid).then { guid }
            whenever(notProperController.guid).then { guid + 1 }


            whenever(localStorage.getDevices()).thenReturn(mutableListOf(
                    device_B,
                    device_A

            ))

            repo.findController(guid).shouldBe(controller)
        }
    }

    @Test
    fun noDevices_findDevice_shouldThrow() {
        runBlocking {
            val repo: DeviceChannelOutput = createRepository(mapOf(
                    device_A.javaClass.simpleName to deviceChannel_A
            ))

            whenever(localStorage.getDevices()).thenReturn(mutableListOf())

            shouldThrow<NoDeviceException> {
                repo.findDevice(controller)
            }
        }
    }

    @Test
    fun hasDevicesWithProperController_findDevice_ShouldReturnDevice() {
        runBlocking {
            val repo: DeviceChannelOutput = createRepository(mapOf(
                    device_A.javaClass.simpleName to deviceChannel_A
            ))

            val guid = 123L
            val notProperController = mock<BaseController>()
            whenever(device_A.controllers).then { listOf(controller) }
            whenever(device_B.controllers).then { listOf(notProperController) }

            whenever(controller.guid).then { guid }
            whenever(notProperController.guid).then { guid + 1 }

            whenever(localStorage.getDevices()).thenReturn(mutableListOf(
                    device_B,
                    device_A

            ))

            repo.findDevice(controller).shouldBe(device_A)
        }
    }
}
