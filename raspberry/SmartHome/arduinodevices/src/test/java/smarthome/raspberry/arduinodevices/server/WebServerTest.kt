package smarthome.raspberry.arduinodevices.server

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import fi.iki.elonen.NanoHTTPD
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.library.common.BaseController
import smarthome.library.common.DeviceChannelOutput
import smarthome.raspberry.arduinodevices.StringValueState

class WebServerTest {

    private val session: NanoHTTPD.IHTTPSession = mock()
    private val deviceChannelOutput: DeviceChannelOutput = mock()
    private val handler = WebHandler(deviceChannelOutput)
    private val controller = BaseController("")

    @Before
    fun setUp() {
        whenever(session.method).thenReturn(NanoHTTPD.Method.POST)
        whenever(session.uri).thenReturn("/alert")
        whenever(session.parms).thenReturn(mapOf(
                "controller_guid" to "123",
                "value" to "11.1"

        ))
        runBlocking {
            whenever(deviceChannelOutput.findController(any())).thenReturn(controller)
        }

    }

    @org.junit.Test
    fun handleAlertShouldTriggerOutputBoundary() {

        runBlocking {
            handler.handle(session)
            verify(deviceChannelOutput).onNewState(controller)
        }
    }

    @Test
    fun handleAlertProvidesValidState() {
        runBlocking {
            handler.handle(session)
            assert((controller.state as StringValueState).value == "11.1")
        }
    }
}