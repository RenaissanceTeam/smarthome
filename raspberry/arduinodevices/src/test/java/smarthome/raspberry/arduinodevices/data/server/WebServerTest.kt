package smarthome.raspberry.arduinodevices.data.server

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoHTTPD.Method
import fi.iki.elonen.NanoHTTPD.Response
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class WebServerTest {
    private lateinit var nanoHTTPD: NanoHTTPD
    private lateinit var webServer: WebServer
    
    @Before
    fun setUp() {
        nanoHTTPD = mock {}
        webServer = WebServerImpl(nanoHTTPD)
    }
    
    @Test
    fun `when no handler found for request should return 404`() {
        val request = RequestIdentifier(Method.GET, "/")
        val result = webServer.serve(request)
        
        assertThat(result.status).isEqualTo(Response.Status.NOT_FOUND)
    }
    
    @Test
    fun `when add handler for get request should use it for get request`() {
        val request = RequestIdentifier(Method.GET, "/")
        val handler = mock<RequestHandler> {
            on { identifier }.then { request }
        }
        
        webServer.setHandler(handler)
        webServer.serve(request)
        
        runBlocking {
            verify(handler).serve()
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
        val result = webServer.serve(requestPost)
        
        assertThat(result.status).isEqualTo(Response.Status.NOT_FOUND)
    }
    
    @Test
    fun `matching handler should use the same uri path`() {
        val invalidPaths = listOf("/", "/a", "/b")
        val validPath = "c"
        val validResponse = mock<Response> {}
        val method = Method.GET
    
        val handler = mock<RequestHandler> {
            on { identifier }.then { RequestIdentifier(method, validPath) }
            on { runBlocking { serve() } }.then { validResponse }
        }
        
        webServer.setHandler(handler)
    
        for (path in invalidPaths) {
            val response = webServer.serve(RequestIdentifier(method, path))
            assertThat(response).isEqualTo(RESPONSE_NOT_FOUND)
        }
        
        assertThat(webServer.serve(RequestIdentifier(method, validPath))).isEqualTo(validResponse)
    }
}