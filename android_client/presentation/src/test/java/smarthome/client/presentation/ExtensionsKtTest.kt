package smarthome.client.presentation

import com.google.common.truth.Truth.assertThat
import org.junit.Test

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
}