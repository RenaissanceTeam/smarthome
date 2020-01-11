package smarthome.client.data.controllers

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.client.data.api.controllers.ControllersRepo
import smarthome.client.data.retrofit.RetrofitFactory
import smarthome.client.util.EmptyStatus
import smarthome.client.util.ErrorStatus
import smarthome.client.util.LoadingStatus

class ControllersRepoImplTest {
    private lateinit var repo: ControllersRepo
    private lateinit var retrofitFactory: RetrofitFactory
    private lateinit var controllersApi: ControllersApi
    @Before
    fun setUp() {
        controllersApi = mock {}
        retrofitFactory = mock {
            on { createApi(ControllersApi::class.java) }.then { controllersApi }
        }
        repo = ControllersRepoImpl(retrofitFactory)
    }
    
    @Test
    fun `when observe device should start with empty`() {
        repo.observe(1).test().assertValue { it is EmptyStatus }
    }
    
    @Test
    fun `when fetching other device should not emit`() {
        val result = repo.observe(1).test()
        
        runBlocking { repo.get(2) }
        result.assertValueCount(1)
            .assertValue { it is EmptyStatus }
    }
    
    @Test
    fun `when fetching device should emit loading `() {
        val result = repo.observe(1).test()
        runBlocking { repo.get(1) }
    
        result.assertValueAt(0) { it is EmptyStatus }
            .assertValueAt(1) { it is LoadingStatus }
    }
    
    @Test
    fun `when fetching controller fails should emit error data status`() {
        val id = 1L
        val result = repo.observe(id).test()
        
        runBlocking {
            whenever(controllersApi.getDetails(id)).then { throw Throwable() }
            repo.runCatching { get(id) }
        }
    
        result.assertValueAt(0) { it is EmptyStatus }
            .assertValueAt(1) { it is LoadingStatus }
            .assertValueAt(2) { it is ErrorStatus }
    }
}