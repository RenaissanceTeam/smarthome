package smarthome.raspberry.util.persistence

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class StorageHelperTest {
    
    private lateinit var storage: PersistentStorage
    private lateinit var storageHelper: StorageHelper
    
    @Before
    fun setUp() {
        storage = mock {}
        storageHelper = StorageHelper(storage)
    }
    
    @Test
    fun `when set new value for key should emit value for observable`() {
        val first = "first"
        val second = "second"
        val key = "key"
        
        val result = storageHelper.observe<String>(key).test()

        runBlocking {
            storageHelper.set(key, first)
            storageHelper.set(key, second)
        }

        result
            .assertValueAt(0, first)
            .assertValueAt(1, second)
    }
    

    @Test
    fun `when set new value to preference it should hold it and return on read`() {
        val value = "a"
        val key = "key"
        runBlocking {
            storageHelper.set(key, value)
        }
        whenever(storage.get(key, String::class)).then { value }
        val result = storageHelper.get<String>(key)

        assertThat(result).isEqualTo(value)
    }
    @Test
    fun `when trying to read preference that is not stored should throw`() {
        val key = "something"
        whenever(storage.get<String>(key)).then { throw NoStoredPreference(key) }
        
        assertFailsWith<NoStoredPreference> {
            storageHelper.get<String>(key)
        }
    }
    
    @Test
    fun `when read preference of type other than expected should throw`() {
        runBlocking {
            storageHelper.set("int", 1)
        }
        whenever(storage.get("int", Int::class)).then { 0 }
        assertFailsWith<IllegalArgumentException> {
            val result = storageHelper.get<String>("int")
        }
    }
    
    @Test
    fun `when set values of type other than already saved should throw`() {
        runBlocking {
            storageHelper.set("int", 1)
            
            assertFailsWith<IllegalArgumentException> {
                storageHelper.set("int", "str")
            }
        }
    }
    
    @Test
    fun `when get and set preference should make use of persistance storage`() {
        val key = "int"
        val value = 1
        
        runBlocking {
            whenever(storage.get(key, Int::class)).then { 0 }
    
            storageHelper.set(key, value)
            verify(storage).set(key, value)
            
            storageHelper.get<Int>(key)
            verify(storage).get<Int>(key)
        }
    }
    
    @Test
    fun `when default saved to preference should return in on read`() {
        val default = 1
        val key = "int"
        
        storageHelper.setDefault(key, default)
        whenever(storage.get<Int>(key)).then { throw NoStoredPreference(key) }
        val result: Int = storageHelper.get(key)
        
        assertThat(result).isEqualTo(default)
    }
}