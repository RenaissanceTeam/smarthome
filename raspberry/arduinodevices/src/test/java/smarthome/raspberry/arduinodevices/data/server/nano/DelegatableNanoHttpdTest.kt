package smarthome.raspberry.arduinodevices.data.server.nano

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import fi.iki.elonen.NanoHTTPD
import org.junit.Before
import org.junit.Test

class DelegatableNanoHttpdTest {
    private lateinit var delegatableNanoHttpd: DelegatableNanoHttpd
    
    @Before
    fun setUp() {
        delegatableNanoHttpd = DelegatableNanoHttpd()
    }
    
    @Test
    fun `when no delegate set should return internal server error`() {
        val request = mock<NanoHTTPD.IHTTPSession>{}
        val result = delegatableNanoHttpd.serve(request)
        
        assertThat(result.status).isEqualTo(NanoHTTPD.Response.Status.INTERNAL_ERROR)
    }
    
    @Test
    fun `when has delegate should use it to process request`() {
        val request = mock<NanoHTTPD.IHTTPSession>{}
        val delegate = mock<NanoRequestDelegate>{}
        
        delegatableNanoHttpd.setDelegate(delegate)
        delegatableNanoHttpd.serve(request)
        
        verify(delegate).invoke(request)
    }
}
