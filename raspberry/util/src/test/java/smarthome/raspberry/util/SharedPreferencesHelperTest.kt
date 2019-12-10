package smarthome.raspberry.util

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.util.persistence.PersistentStorage
import smarthome.raspberry.util.persistence.SharedPreferencesHelper
import smarthome.raspberry.util.persistence.get
import smarthome.raspberry.util.persistence.set
import kotlin.test.assertFailsWith

class SharedPreferencesHelperTest {
    
    private lateinit var storage: PersistentStorage
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    
    @Before
    fun setUp() {
        storage = mock {}
        sharedPreferencesHelper = SharedPreferencesHelper(storage)
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
        runBlocking {
            sharedPreferencesHelper.set(key, value)
        }
        whenever(storage.get(key, String::class)).then { value}
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
        runBlocking {
            sharedPreferencesHelper.set("int", 1)
        }
        whenever(storage.get("int", Int::class)).then { 0 }
        assertFailsWith<IllegalArgumentException> {
            val result = sharedPreferencesHelper.get<String>("int")
        }
    }
    
    @Test
    fun `when set values of type other than already saved should throw`() {
        runBlocking {
            sharedPreferencesHelper.set("int", 1)
            
            assertFailsWith<IllegalArgumentException> {
                sharedPreferencesHelper.set("int", "str")
            }
        }
    }
    
    @Test
    fun `when get and set preference should make use of persistance storage`() {
        val key = "int"
        val value = 1
        
        runBlocking {
            whenever(storage.get(key, Int::class)).then { 0 }
    
            sharedPreferencesHelper.set(key, value)
            verify(storage).set(key, value)
            
            sharedPreferencesHelper.get<Int>(key)
            verify(storage).get<Int>(key)
        }
    }
}