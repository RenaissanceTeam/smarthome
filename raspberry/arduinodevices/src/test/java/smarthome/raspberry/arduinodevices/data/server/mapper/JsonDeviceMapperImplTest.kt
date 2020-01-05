package smarthome.raspberry.arduinodevices.data.server.mapper

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import org.junit.Before
import org.junit.Test
import smarthome.library.common.BaseController
import smarthome.library.common.ControllerState
import smarthome.library.common.Id
import smarthome.library.common.IotDevice
import smarthome.library.common.constants.typeField
import smarthome.library.common.util.RuntimeTypeAdapterFactory
import smarthome.raspberry.arduinodevices.data.server.entity.InvalidDeviceException
import smarthome.raspberry.arduinodevices.domain.ArduinoDevice
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SupportedDeviceA(
    id: Id, name: String, description: String?, controllers: List<BaseController>, ip: String
) : ArduinoDevice(id, name, description, controllers, ip)

class SupportedDeviceB(
    id: Id, name: String, description: String?, controllers: List<BaseController>, ip: String
) : ArduinoDevice(id, name, description, controllers, ip)

class NotSupportedDeviceC(
    id: Id, name: String, description: String?, controllers: List<BaseController>, ip: String
) : ArduinoDevice(id, name, description, controllers, ip)

class SupportedControllerA(name: String): BaseController(name)
class SupportedControllerB(name: String): BaseController(name)
class NotSupportedControllerC(name: String): BaseController(name)

class SupportedControllerStateA(val value: String) : ControllerState()
class SupportedControllerStateB(@SerializedName("value") val intValue: Int) : ControllerState()

class JsonDeviceMapperImplTest {
    
    private lateinit var mapper: JsonDeviceMapper
    private lateinit var gson: Gson
    
    @Before
    fun setUp() {
        gson = GsonBuilder()
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory
                    .of(IotDevice::class.java, typeField)
                    .registerSubtype(SupportedDeviceA::class.java, "A")
                    .registerSubtype(SupportedDeviceB::class.java, "B")
            )
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory
                    .of(BaseController::class.java, typeField)
                    .registerSubtype(SupportedControllerA::class.java, "A")
                    .registerSubtype(SupportedControllerB::class.java, "B")
            )
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory
                    .of(ControllerState::class.java, typeField)
                    .registerSubtype(SupportedControllerStateA::class.java, "A")
                    .registerSubtype(SupportedControllerStateB::class.java, "B")
            )
    
            .create()
        mapper = JsonDeviceMapperImpl(gson)
    }
    
    @Test
    fun `when can't parse json should throw`() {
        val invalidJson = "invalid"
        
        assertFailsWith<InvalidDeviceException> { mapper.map(invalidJson) }
    }
    
    @Test
    fun `when pass invalid type should throw`() {
        val device = """
            {
                $typeField: "C",
                "id": { "id": "" },
                "name": "",
                "controllers": [
                    "id": { "id": "" },
                    "name": "",
                    "state": { }
                ]
            }
        """.trimIndent()
        
        assertFailsWith<InvalidDeviceException> { mapper.map(device) }
    }
    
    @Test
    fun `when pass device of valid type should successfully map`() {
        val device = """
            {
                $typeField: "A",
                "id": { "id": "" },
                "name": "",
                "controllers": [ {
                        $typeField: "A",
                        "id": { "id": "" },
                        "name": "",
                        "state": { $typeField: "A", "value": "1" }
                    }
                ]
            }
        """.trimIndent()
        
        val result = mapper.map(device)
        
        assertTrue { result is SupportedDeviceA }
    }
    
    @Test
    fun `parsed id should be correct`() {
        val id = Id("some")
        val device = """
            {
                $typeField: "A",
                "id": { "id": "some" },
                "name": "",
                "controllers": [ {
                        $typeField: "A",
                        "id": { "id": "" },
                        "name": "",
                        "state": { $typeField: "A", "value": "1" }
                    }
                ]
            }
        """.trimIndent()
    
        val result = mapper.map(device)
    
        assertThat(result.id).isEqualTo(id)
    }
    
    @Test
    fun `parsed controllers should be correct`() {
        val device = """
            {
                $typeField: "A",
                "id": { "id": "some" },
                "name": "",
                "controllers": [ {
                        $typeField: "B",
                        "id": { "id": "" },
                        "name": "",
                        "state": { $typeField: "A", "value": "1" }
                    }
                ]
            }
        """.trimIndent()
        
        val result = mapper.map(device)
        val controller = result.controllers.first()
        assertTrue { controller is SupportedControllerB }
    }
    
    @Test
    fun `parsed state should be correct`() {
        val device = """
            {
                $typeField: "A",
                "id": { "id": "some" },
                "name": "",
                "controllers": [ {
                        "type": "B",
                        "id": { "id": "" },
                        "name": "",
                        "state": { "type": "A", "value": "1" }
                    }, {
                        "type": "A",
                        "id": { "id": "123" },
                        "name": "",
                        "state": { "type": "B", "value": 2 }
                    }
                ]
            }
        """.trimIndent()
    
        val result = mapper.map(device)
        val stateA = result.controllers[0].state
        val stateB = result.controllers[1].state
        
        assertTrue { stateA is SupportedControllerStateA }
        assertTrue { stateB is SupportedControllerStateB }
        
        assertThat((stateA as SupportedControllerStateA).value).isEqualTo("1")
        assertThat((stateB as SupportedControllerStateB).intValue).isEqualTo(2)
    }
}