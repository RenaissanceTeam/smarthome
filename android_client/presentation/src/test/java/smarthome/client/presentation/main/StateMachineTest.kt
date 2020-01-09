package smarthome.client.presentation.main

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import smarthome.client.util.log

class StateMachineTest {
    
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
        machine = StateMachine(login, homeServer, needHomeServer, needLogin)
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
        login.set(false)
        login.set(true)
        verify(needLogin, times(2)).invoke()
    }
}