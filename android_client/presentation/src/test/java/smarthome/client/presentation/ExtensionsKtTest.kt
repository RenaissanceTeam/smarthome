package smarthome.client.presentation

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExtensionsKtTest {
    
    @Test
    fun `replace first item`() {
        val before = listOf(1, 2, 3)
        assertThat(before.replace(2) { it == 1 }).isEqualTo(listOf(2,2,3))
    }
    
    @Test
    fun `replace last item`() {
        val before = listOf(1, 2, 3)
        assertThat(before.replace(2) { it == 3 }).isEqualTo(listOf(1,2,2))
    }
    
    @Test
    fun `withRemoved should return list that's size is less by one and missing removed item`() {
        val before = listOf(1, 2, 3)
        val after = before.withRemoved { it == 2 }
        assertTrue {
            after == listOf(1, 3)
        }
    }
    
    @Test
    fun `find and modify should return new list with replaced value`() {
        val before = listOf(1, 2, 3)
        val after = before.findAndModify({ it == 1 }) {
            it + 1
        }
        
        assertEquals(listOf(2, 2, 3), after)
    }
}