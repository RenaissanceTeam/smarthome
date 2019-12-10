package smarthome.raspberry.util

import android.content.Context
import android.content.SharedPreferences
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

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
    
//    @Test
//    fun `when set new value for key should emit value for observable`() {
//        val first = "first"
//        val second = "second"
//
//        val result = sharedPreferencesHelper.observe<String>("key").test()
//
//        runBlocking {
//            sharedPreferencesHelper.setString("key", first)
//            sharedPreferencesHelper.setString("key", second)
//        }
//
//        result
//            .assertValueAt(0, first)
//            .assertValueAt(1, second)
//    }
    
    @Test
    fun `when set new value to preference it should hold it and return on read`() {
        val value = "a"
        val key = "key"
        sharedPreferencesHelper.set(key, value)
        
        val result = sharedPreferencesHelper.get<String>(key)
        
        assertThat(result).isEqualTo(value)
    }
    
    @Test
    fun `when trying to read preference that is not stored should throw`() {
        assertFailsWith<IllegalArgumentException> {
            sharedPreferencesHelper.get<String>("something")
        }
    }
    
    @Test
    fun `when read preference of type other than expected should throw`() {
        sharedPreferencesHelper.set("int", 1)
        
        assertFailsWith<IllegalArgumentException> {
            val result = sharedPreferencesHelper.get<String>("int")
        }
    }
    
    
    
    
}