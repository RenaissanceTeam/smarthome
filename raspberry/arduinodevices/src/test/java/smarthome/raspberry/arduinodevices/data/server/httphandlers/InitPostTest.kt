package smarthome.raspberry.arduinodevices.data.server.httphandlers

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.arduinodevices.data.server.entity.BAD_REQUEST_CODE
import smarthome.raspberry.arduinodevices.data.server.entity.Method
import smarthome.raspberry.arduinodevices.data.server.entity.Request
import smarthome.raspberry.arduinodevices.data.server.entity.RequestIdentifier
import smarthome.raspberry.arduinodevices.data.server.mapper.JsonDeviceMapper
import smarthome.raspberry.arduinodevices.data.server.requestWith

class InitPostTest {
    
    private lateinit var initPost: InitPost
    private lateinit var mapper: JsonDeviceMapper
    
    @Before
    fun setUp() {
        mapper = mock {}
        
        initPost = InitPost(mapper)
    }
    
    @Test
    fun `when empty body should return 404`() {
        val result = runBlocking { initPost.serve(requestWith(body = "")) }
        
        assertThat(result.code).isEqualTo(BAD_REQUEST_CODE)
    }
}