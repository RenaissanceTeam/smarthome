package smarthome.raspberry.home.domain

import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import smarthome.raspberry.home.api.domain.HomeStateMachine
import smarthome.raspberry.home.api.domain.eventbus.events.*
import smarthome.raspberry.home.data.EventBusRepository

class HomeStateMachineImplTest {
    
    
    private lateinit var machine: HomeStateMachine
    private lateinit var eventsRepo: EventBusRepository
    
    @Before
    fun setUp() {
        eventsRepo = mock {  }
        machine = HomeStateMachineImpl(eventsRepo)
    }
    
    @Test
    fun `start with putting home to paused state`() {
        verify(eventsRepo).addEvent(argThat { this is Paused })
    }
    
    @Test
    fun `start with need for user`() {
        verify(eventsRepo).addEvent(argThat { this is NeedUser })
    }
    
    @Test
    fun `when has user express need for home id`() {
        machine.registerEvent(HasUser())
        verify(eventsRepo).addEvent(argThat { this is NeedHomeIdentifier })
    }
    
    @Test
    fun `when has user and home id put home in resumed state`() {
        machine.registerEvent(HasUser())
        machine.registerEvent(HasHomeIdentifier())
        verify(eventsRepo).addEvent(argThat { this is Resumed })
    }
}