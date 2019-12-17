package smarthome.raspberry.arduinodevices.data.server.httphandlers

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.library.common.BaseController
import smarthome.library.common.ControllerState
import smarthome.library.common.Id
import smarthome.raspberry.arduinodevices.data.server.entity.BAD_REQUEST_CODE
import smarthome.raspberry.arduinodevices.data.server.entity.RequestIdentifier
import smarthome.raspberry.arduinodevices.data.server.entity.SUCCESS_CODE
import smarthome.raspberry.arduinodevices.data.server.mapper.ValuePayloadToControllerStateMapper
import smarthome.raspberry.arduinodevices.data.server.requestWith
import smarthome.raspberry.controllers.api.domain.GetControllerByIdUseCase
import smarthome.raspberry.controllers.api.domain.OnControllerChangedWithoutUserRequestUseCase

class AlertPostTest {
    
    private lateinit var alertPost: AlertPost
    private lateinit var onControllerChangedUC: OnControllerChangedWithoutUserRequestUseCase
    private lateinit var getControllerByIdUseCase: GetControllerByIdUseCase
    private lateinit var valueMapper: ValuePayloadToControllerStateMapper
    
    @Before
    fun setUp() {
        onControllerChangedUC = mock {}
        getControllerByIdUseCase = mock {}
        valueMapper = mock {}
        alertPost = AlertPost(getControllerByIdUseCase, onControllerChangedUC, valueMapper)
    }
    
    
    @Test
    fun `when improper guid passed should return bad request`() {
        val result = runBlocking {
            alertPost.serve(requestWith(mapOf(controllerId to "", value to "value")))
        }
        
        assertThat(result.code).isEqualTo(BAD_REQUEST_CODE)
    }
    
    @Test
    fun `should return success code when everything ok`() {
        val result = runBlocking {
            alertPost.serve(requestWith(mapOf(controllerId to "1", value to "value")))
        }
        
        assertThat(result.code).isEqualTo(SUCCESS_CODE)
    }
    
    @Test
    fun `when serve() should trigger OnControllerChanged use case`() {
        runBlocking {
            val controller = mock<BaseController>()
            val state = mock<ControllerState>()
            
            whenever(getControllerByIdUseCase.execute(Id(("1")))).then { controller }
            whenever(valueMapper.map("value")).then { state }
    
            alertPost.serve(requestWith(mapOf(controllerId to "1", value to "value")))
            
            verify(onControllerChangedUC).execute(controller, state)
        }
    }
}