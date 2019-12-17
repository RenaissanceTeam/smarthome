package smarthome.raspberry.arduinodevices.data.server.httphandlers

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.library.common.IotDevice
import smarthome.raspberry.arduinodevices.data.server.entity.BAD_REQUEST_CODE
import smarthome.raspberry.arduinodevices.data.server.entity.Method
import smarthome.raspberry.arduinodevices.data.server.entity.Request
import smarthome.raspberry.arduinodevices.data.server.entity.RequestIdentifier
import smarthome.raspberry.arduinodevices.data.server.mapper.JsonDeviceMapper
import smarthome.raspberry.arduinodevices.data.server.requestWith
import smarthome.raspberry.devices.api.domain.AddDeviceUseCase

class InitPostTest {
    
    private lateinit var initPost: InitPost
    private lateinit var addDeviceUseCase: AddDeviceUseCase
    private lateinit var mapper: JsonDeviceMapper
    
    @Before
    fun setUp() {
        mapper = mock {}
        addDeviceUseCase = mock {}
        
        initPost = InitPost(mapper, addDeviceUseCase)
    }
    
    @Test
    fun `when empty body should return 404`() {
        val result = runBlocking { initPost.serve(requestWith(body = "")) }
        
        assertThat(result.code).isEqualTo(BAD_REQUEST_CODE)
    }
    
    @Test
    fun `should call add new device after mapping body`() {
        runBlocking {
            val json = "{}"
            val device = mock<IotDevice>{}
            whenever(mapper.map(json)).then { device }
            initPost.serve(requestWith(json))
            
            verify(addDeviceUseCase).execute(device)
        }
    }
}