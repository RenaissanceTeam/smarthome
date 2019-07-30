package smarthome.raspberry.arduinodevices.server

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import fi.iki.elonen.NanoHTTPD
import kotlinx.coroutines.runBlocking
import org.junit.Test
import smarthome.library.common.BaseController
import smarthome.library.common.DeviceChannelOutput

class WebServerTest {

    private val session: NanoHTTPD.IHTTPSession = mock()
    private val deviceChannelOutput: DeviceChannelOutput = mock()
    private val handler = WebHandler(deviceChannelOutput)

    @org.junit.Test
    fun handleAlertShouldTriggerOutputBoundary() {

        runBlocking {
            val controller = BaseController("")
            whenever(session.method).thenReturn(NanoHTTPD.Method.POST)
            whenever(session.uri).thenReturn("/alert")
            whenever(session.parms).thenReturn(mapOf(
                    "controller_guid" to "123"
            ))
            whenever(deviceChannelOutput.findController(any())).thenReturn(controller)

            handler.handle(session)

            verify(deviceChannelOutput).onNewState(controller)
        }
    }

  
}