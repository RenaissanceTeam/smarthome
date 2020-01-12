package smarthome.client.data.controllers

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.client.data.api.controllers.ControllersRepo
import smarthome.client.data.controllers.dto.StateDto
import smarthome.client.data.retrofit.RetrofitFactory
import smarthome.client.data.util.assertThroughSequence
import smarthome.client.entity.Controller
import smarthome.client.util.Data
import smarthome.client.util.EmptyStatus
import smarthome.client.util.ErrorStatus
import smarthome.client.util.LoadingStatus

class ControllersRepoImplTest {
    private lateinit var repo: ControllersRepo
    private lateinit var retrofitFactory: RetrofitFactory
    private lateinit var controllersApi: ControllersApi
    @Before
    fun setUp() {
        controllersApi = mock {
            onBlocking { getDetails(1L) }.then {
                Controller(1L, 2, "", "", "")
            }
            
            onBlocking { changeState(1L, StateDto("mockedState")) }.then { StateDto("mockedState") }
        }
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
    
    @Test
    fun `when try to change state of controller that is not in repo should first get details`() {
        val id = 1L
        val stateToChange = "mockedState"
        val observable = repo.observe(id).test()
        
        runBlocking {
            observable.assertValue { it is EmptyStatus }
            repo.setState(id, stateToChange)
            
            verify(controllersApi).getDetails(id)
        }
    }
    
    @Test
    fun `when change state of controller that is already in repo should not get details`() {
        val id = 1L
        val observable = repo.observe(id).test()
        
        runBlocking {
            repo.get(id)
            observable
                .assertValueAt(2) { it is Data }
            verify(controllersApi).getDetails(id)
            
            repo.setState(id, "mockedState")
            verify(controllersApi).changeState(id, StateDto("mockedState"))
            verifyNoMoreInteractions(controllersApi)
        }
    }
    
    @Test
    fun `when set to loading status should pass last data if exists`() {
        val id = 1L
        val observable = repo.observe(id).test()
        
        runBlocking {
            repo.get(id)
            observable.assertValueAt(2) { it is Data }
            verify(controllersApi).getDetails(id)
            
            repo.setState(id, "mockedState")
            observable
                .assertValueAt(3) { it is LoadingStatus && it.lastData != null }
        }
    }
    
    @Test
    fun `when setState() should emit loading, then emit data with new state if no error`() {
        val id = 1L
        val stateToChange = "mockedState"
        val observable = repo.observe(id).test()
        runBlocking {
            val result = repo.setState(id, stateToChange)
            
            observable
                .assertThroughSequence(
                    { it is LoadingStatus },
                    { it is Data && it.data.state == stateToChange }
                )
            
            assertThat(result).isEqualTo(stateToChange)
        }
        
    }
}