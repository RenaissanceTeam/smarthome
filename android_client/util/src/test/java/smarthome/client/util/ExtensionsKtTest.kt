package smarthome.client.util

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
    fun `when remove from list of size 1 should return empty list`() {
        val before = listOf(1)
        assertTrue { before.withRemoved { it == 1 }.isEmpty() }
    }
    
    @Test
    fun `find and modify should return new list with replaced value`() {
        val before = listOf(1, 2, 3)
        val after = before.findAndModify({ it == 1 }) {
            it + 1
        }
        
        assertEquals(listOf(2, 2, 3), after)
    }
    
    @Test
    fun `insert into list`() {
        val before = listOf(1, 2, 3)
        val after = before.withInserted(index = 1, item = 9)
        
        assertEquals(4, after.size)
        assertEquals(9, after[1])
    }
    
    @Test
    fun `when filter remove mutable list should be changed`() {
        val before = mutableListOf(1, 2, 3)
        val shouldBe = mutableListOf(1, 2)
        
        before.filterRemove { it == 3 }
        
        assertEquals(2, before.size)
        assertEquals(shouldBe, before)
    }
}