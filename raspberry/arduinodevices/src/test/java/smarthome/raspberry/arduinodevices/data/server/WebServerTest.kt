package smarthome.raspberry.arduinodevices.data.server

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.arduinodevices.data.server.api.RequestHandler
import smarthome.raspberry.arduinodevices.data.server.api.WebServer
import smarthome.raspberry.arduinodevices.data.server.api.WebServerGate
import smarthome.raspberry.arduinodevices.data.server.entity.*

class MockWebServerGate : WebServerGate {
    private lateinit var action: (Request) -> Response
    override fun start() {}
    override fun stop() {}
    override fun setOnRequest(action: (Request) -> Response) {
        this.action = action
    }
    
    fun trigger(request: RequestIdentifier, params: Map<String, String> = emptyMap()) =
        action(requestWith(request, params))
}


class WebServerTest {
    private lateinit var webServer: WebServer
    private lateinit var webServerGate: MockWebServerGate
    
    @Before
    fun setUp() {
        webServerGate = MockWebServerGate()
        webServer = WebServerImpl(webServerGate)
    }
    
    @Test
    fun `when no handler found for request should return 404`() {
        val request = RequestIdentifier(Method.GET, "/")
        webServer.start()
        val result = webServerGate.trigger(request)
    
        assertThat(result).isEqualTo(notFound)
    }
    
    @Test
    fun `when add handler for get request should use it for get request`() {
        val request = RequestIdentifier(Method.GET, "/")
        val handler = mock<RequestHandler> {
            on { identifier }.then { request }
        }
        
        webServer.setHandler(handler)
        webServer.start()
        webServerGate.trigger(request)
    
        runBlocking {
            verify(handler).serve(Request(request))
        }
    }
    
    @Test
    fun `when add handler for get should return 404 for post request`() {
        val requestGet = RequestIdentifier(Method.GET, "/")
        val requestPost = RequestIdentifier(Method.POST, "/")
        
        val handler = mock<RequestHandler>{
            on { identifier }.then { requestGet }
        }
    
        webServer.setHandler(handler)
        webServer.start()
        val result = webServerGate.trigger(requestPost)
    
        assertThat(result).isEqualTo(notFound)
    }
    
    @Test
    fun `matching handler should use the same uri path`() {
        val invalidPaths = listOf("/", "/a", "/b")
        val validPath = "c"
        val validResponse = mock<Response> {}
        val method = Method.GET
    
        val handler = mock<RequestHandler> {
            on { identifier }.then {
                RequestIdentifier(method, validPath)
            }
            on { runBlocking { serve(any()) } }.then { validResponse }
        }
    
        webServer.start()
        webServer.setHandler(handler)
        
        for (path in invalidPaths) {
            val response = webServerGate.trigger(RequestIdentifier(method, path))
            assertThat(response).isEqualTo(notFound)
        }
    
        val response = webServerGate.trigger(RequestIdentifier(method, validPath))
        assertThat(response).isEqualTo(validResponse)
    }
    
    @Test
    fun `if handler doesn't contain parameter should return not found`() {
        val validPath = "c"
        val method = Method.GET
        
        val handler = mock<RequestHandler> {
            on { identifier }.then {
                RequestIdentifier(method, validPath)
            }
        }
        
        webServer.start()
        webServer.setHandler(handler)
        
        val response = webServerGate.trigger(
            RequestIdentifier(
                method,
                validPath,
                parameters = setOf("key")
            )
        )
        
        
        assertThat(response.code).isEqualTo(NOT_FOUND_CODE)
    }
    
    @Test
    fun `when parameters provided are not the same as handler expects should return 404`() {
        val validPath = "c"
        val method = Method.GET
    
        val handler = mock<RequestHandler> {
            on { identifier }.then {
                RequestIdentifier(method, validPath, setOf("a"))
            }
        }
    
        webServer.start()
        webServer.setHandler(handler)
    
        val response = webServerGate.trigger(
            RequestIdentifier(
                method,
                validPath,
                setOf("b")
            )
        )
    
        assertThat(response.code).isEqualTo(NOT_FOUND_CODE)
    }
    
    @Test
    fun `if handler contains all parameters should use it for request`() {
        val validPath = "c"
        val method = Method.GET
        val obliged = setOf("a", "b")
    
        val handler = mock<RequestHandler> {
            on { identifier }.then {
                RequestIdentifier(method, validPath, obliged)
            }
        }
    
        webServer.start()
        webServer.setHandler(handler)
    
        val response = webServerGate.trigger(
            RequestIdentifier(method, validPath, parameters = setOf("a", "b")),
            params = mapOf(
                "a" to "aval",
                "b" to "bval"
            )
        
        )
    
        runBlocking {
            verify(handler).serve(requestWith(
                RequestIdentifier(method, validPath, parameters = setOf("a", "b")),
                mapOf("a" to "aval", "b" to "bval")
            ))
        }
    }
    
}