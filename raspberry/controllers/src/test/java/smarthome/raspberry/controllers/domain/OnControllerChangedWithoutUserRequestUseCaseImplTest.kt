package smarthome.raspberry.controllers.domain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import smarthome.library.common.Controller
import smarthome.library.common.ControllerState
import smarthome.raspberry.controllers.api.domain.OnControllerChangedWithoutUserRequestUseCase
import smarthome.raspberry.devices.api.domain.GetDeviceByControllerUseCase
import smarthome.raspberry.devices.api.domain.SaveDeviceUseCase

class OnControllerChangedWithoutUserRequestUseCaseImplTest {
    
    private lateinit var useCase: OnControllerChangedWithoutUserRequestUseCase
    private lateinit var getDeviceUC: GetDeviceByControllerUseCase
    private lateinit var saveDeviceUseCase: SaveDeviceUseCase
    
    @Before
    fun setUp() {
        
        getDeviceUC = mock {}
        saveDeviceUseCase = mock {}
        useCase = OnControllerChangedWithoutUserRequestUseCaseImpl(
            getDeviceUC, saveDeviceUseCase
        )
    }
    
    @Test
    fun `controller state should be updated after execute`() {
        val controller = mock<Controller>()
        val newState = mock<ControllerState>()
        runBlocking {
            useCase.execute(controller, newState)
            
            verify(controller).state = newState
        }
        
    }
}