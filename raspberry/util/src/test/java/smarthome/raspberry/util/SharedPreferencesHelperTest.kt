package smarthome.raspberry.util

import android.content.Context
import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SharedPreferencesHelperTest {
    
    private lateinit var context: Context
    private lateinit var mockPrefs: SharedPreferences
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    
    @Before
    fun setUp() {
        mockPrefs = mock {}
        context = mock {
            on { getSharedPreferences(any(), any()) }.thenReturn(mockPrefs)
        }
        sharedPreferencesHelper = SharedPreferencesHelper(context)
    }
    
    @Test
    fun `when set new value for key should emit value for observable`() {
        val first = "first"
        val second = "second"
        
        val result = sharedPreferencesHelper.observe<String>("key").test()
        
        runBlocking {
            sharedPreferencesHelper.setString("key", first)
            sharedPreferencesHelper.setString("key", second)
        }
        
        result
            .assertValueAt(0, first)
            .assertValueAt(1, second)
    }
}