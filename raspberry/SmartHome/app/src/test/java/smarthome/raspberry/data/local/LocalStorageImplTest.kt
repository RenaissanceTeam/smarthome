package smarthome.raspberry.data.local

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory
import com.nhaarman.mockitokotlin2.mock
import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.matchers.collections.shouldContainExactly
import io.kotlintest.matchers.types.shouldBeTypeOf
import io.kotlintest.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.Test
import smarthome.library.common.BaseController
import smarthome.library.common.DeviceServeState
import smarthome.library.common.IotDevice
import smarthome.raspberry.data.LocalStorage
import smarthome.raspberry.data.LocalStorageInput
import smarthome.raspberry.data.LocalStorageOutput
import smarthome.raspberry.data.local.devices.LocalDevicesStorageImpl


class Device_A(name: String, description: String?, serveState: DeviceServeState, guid: Long,
               controllers: List<BaseController>) :
        IotDevice(name, description, serveState, guid, controllers)

class Device_B(name: String, description: String?, serveState: DeviceServeState, guid: Long,
               controllers: List<BaseController>) :
        IotDevice(name, description, serveState, guid, controllers)


class LocalStorageImplTest {

    private val preferencesHelper = mock<SharedPreferencesHelper>()
    private val input = mock<LocalStorageInput>()
    private val output = mock<LocalStorageOutput>()
    private val adapter = RuntimeTypeAdapterFactory
            .of(IotDevice::class.java)
            .registerSubtype(Device_A::class.java)
            .registerSubtype(Device_B::class.java)
    private val storage: LocalDevicesStorage = LocalDevicesStorageImpl(adapter)

    private fun createStorage(prefsHelper: SharedPreferencesHelper = preferencesHelper,
                              input: LocalStorageInput = this.input,
                              output: LocalStorageOutput = this.output): LocalStorage {
        return LocalStorageImpl(prefsHelper, input, output, storage)
    }

    private fun createDeviceA(name: String = "", description: String? = null,
                              serveState: DeviceServeState = DeviceServeState.IDLE,
                              guid: Long = 123L, controllers: List<BaseController> = listOf()
    ): Device_A {
        return Device_A(name, description, serveState, guid, controllers)
    }

    private fun createDeviceB(name: String = "", description: String? = null,
                              serveState: DeviceServeState = DeviceServeState.IDLE,
                              guid: Long = 124L, controllers: List<BaseController> = listOf()
    ): Device_B {
        return Device_B(name, description, serveState, guid, controllers)
    }

    @Test
    fun noSavedDevices_createLocalStorage_ShouldReturnEmptyDevices() {
        val storage = createStorage()

        storage.getDevices().shouldBeEmpty()
    }

    @Test
    fun oneSavedDevice_createLocalStorage_shouldReturnOneDevice() {
        runBlocking {
            val storage = createStorage()
            val device = createDeviceA()

            storage.addDevice(device)

            val anotherStorage = createStorage()
            anotherStorage.getDevices().shouldContain(device)
        }
    }

    @Test
    fun twoSavedDevicesOfDifferentType_shouldReturnListOfTwoDevices() {
        runBlocking {
            val storage = createStorage()

            val deviceA = createDeviceA()
            val deviceB = createDeviceB()

            storage.addDevice(deviceA)
            storage.addDevice(deviceB)

            val anotherStorage = createStorage()
            val devices = anotherStorage.getDevices()

            devices.shouldContainExactly(mutableListOf(deviceA, deviceB))

            devices.first().shouldBeTypeOf<Device_A>()
            devices.last().shouldBeTypeOf<Device_B>()
        }
    }

    @Test
    fun noSavedDevices_addOneActiveOnePending_shouldReturnListOfOneItem() {
        runBlocking {
            val storage = createStorage()

            val deviceA = createDeviceA()
            val deviceB = createDeviceB()

            storage.addDevice(deviceA)
            storage.addPendingDevice(deviceB)

            val anotherStorage = createStorage()

            anotherStorage.getDevices().shouldContainExactly(deviceA)
            anotherStorage.getPendingDevices().shouldContainExactly(deviceB)
        }
    }

    @Test
    fun haveOneSavedDevice_updateDevice_ShouldPersistNewInfo() {
        runBlocking {
            val storage = createStorage()
            val nameBefore = "before"
            val deviceA = createDeviceA(name = nameBefore)
            storage.addDevice(deviceA)

            val nameAfter = "after"
            deviceA.name = nameAfter
            storage.updateDevice(deviceA)

            val anotherStorage = createStorage()

            anotherStorage.getDevices().first().name.shouldBe(nameAfter)
        }
    }

    @Test
    fun haveOneSavedDevice_removeDevice_ShouldReturnEmptyList() {
        runBlocking {
            val storage = createStorage()
            val deviceA = createDeviceA()
            storage.addDevice(deviceA)

            storage.removeDevice(deviceA)

            val anotherStorage = createStorage()

            anotherStorage.getDevices().shouldBeEmpty()
        }
    }
}