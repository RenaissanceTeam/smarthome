package smarthome.library.common.util

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test
import smarthome.library.common.util.delegates.DependOnChangeable

class DependOnChangeableTest {
    private lateinit var changeable: Holder<String>

    @Before
    fun setup() {
        changeable = mock {}
    }

    @Test
    fun `when get() should use the changeable value for the result`() {
        val current = ""
        whenever(changeable.get()).thenReturn(current)
        val result by DependOnChangeable(changeable) { it }

        assertThat(result).isEqualTo(current)
    }

    @Test
    fun `when changeable doesn't change delegated result should be the same`() {
        val current = ""

        whenever(changeable.get()).thenReturn(current)
        val calculation = mock<(String) -> String>()
        val result by DependOnChangeable(changeable, calculation)

        val a = result
        verify(calculation).invoke(current)
        val b = result
        assertThat(a).isEqualTo(b)
    }

    @Test
    fun `when changeable changes delegated result must be recalculated`() {
        val current = ""
        val changed = "changed"
        whenever(changeable.get()).thenReturn(current)
        val calculation = mock<(String) -> String>()
        val result: String by DependOnChangeable(changeable, calculation)

        val a = result
        verify(calculation).invoke(current)

        whenever(changeable.get()).thenReturn(changed)
        val b = result
        verify(calculation).invoke(changed)
    }

    @Test
    fun `when changeable doesn't change should not recalculate property`() {
        val current = ""
        val changed = "changed"

        whenever(changeable.get()).thenReturn(current)
        val calculation = mock<(String) -> String> {
            on { invoke(any()) }.thenReturn("result")
        }
        val result by DependOnChangeable(changeable, calculation)

        val a = result.toString()
        verify(calculation).invoke(current)
        val b = result.toString()
        verifyNoMoreInteractions(calculation)


        whenever(changeable.get()).thenReturn(changed)
        val c = result.toString()
        verify(calculation).invoke(changed)
    }
}