package smarthome.client.util

import org.junit.Assert.*
import org.junit.Test

class StringExtensionsKtTest {
    
    @Test
    fun `when truncate string that is shorted then max should return string`() {
        assertEquals("abc", "abc".truncate(3))
        assertEquals("abc", "abc".truncate(4))
    }
    
    @Test
    fun `when truncate string that is bigger than max should return string with dots at end`() {
        assertEquals("abc...", "abcdef".truncate(3))
    }
}