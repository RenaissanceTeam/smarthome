package smarthome.client.domain.main

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import smarthome.client.domain.api.main.BooleanState
import smarthome.client.domain.api.main.Need
import smarthome.client.domain.api.main.StateMachine

class StateMachineImplTest {
    
    private lateinit var machine: StateMachine
    private lateinit var login: BooleanState
    private lateinit var homeServer: BooleanState
    private lateinit var needHomeServer: Need
    private lateinit var needLogin: Need
    
    @Before
    fun setUp() {
        login = BooleanState()
        homeServer = BooleanState()
        needHomeServer = mock {}
        needLogin = mock {}
        machine = StateMachineImpl(login, homeServer).apply {
            setOnNeedHomeServer(needHomeServer)
            setOnNeedLogin(needLogin)
        }
    }
    
    @Test
    fun `when home server is in false state should trigger need in home server`() {
        homeServer.set(false) // 1 invoke
        login.set(true) // 2 invoke
        verify(needHomeServer, times(2)).invoke()
    }
    
    @Test
    fun `when home server is true and no login (by default) should trigger need in login`() {
        homeServer.set(true)
        login.set(false) // 1
        login.set(false) // 2
        login.set(true)
        login.set(false) // 3
        verify(needLogin, times(3)).invoke()
    }
    
    @Test
    fun `wait for homeServer state to initialize before triggering needs`() {
        login.set(false)
        homeServer.set(false)
        verify(needHomeServer).invoke()
    }
}