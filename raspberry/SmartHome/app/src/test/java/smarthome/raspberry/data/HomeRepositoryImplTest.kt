package smarthome.raspberry.data

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.verify
import smarthome.library.common.*
import smarthome.raspberry.domain.HomeRepository

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


    @Test
    fun hasController_proceedReadController_ReadStateFromChannel() {
        runBlocking {
            repo = HomeRepositoryImpl(localStorage, remoteStorage, mapOf(
                    device_A.javaClass to deviceChannel_A
            ))

            whenever(localStorage.findDevice(controller)).then { device_A }
            repo.proceedReadController(controller)
            verify(deviceChannel_A).read(device_A, controller)
        }
    }

    @Test
    fun hasTwoChannels_proceedReadController_ReadStateFromProperChannel() {
        runBlocking {
            repo = HomeRepositoryImpl(localStorage, remoteStorage, mapOf(
                    device_A.javaClass to deviceChannel_A,
                    device_B.javaClass to deviceChannel_B
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
            repo = HomeRepositoryImpl(localStorage, remoteStorage, mapOf(
                    device_A.javaClass to deviceChannel_A,
                    device_B.javaClass to deviceChannel_B
            ))

            val state = ControllerState()

            whenever(localStorage.findDevice(any())).then { device_A }
            repo.proceedWriteController(controller, state)
            verify(deviceChannel_A).writeState(device_A, controller, state)
        }
    }
}
