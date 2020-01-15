package smarthome.client.domain.conrollers.usecases

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import smarthome.client.data.api.controllers.ControllersRepo
import smarthome.client.domain.api.conrollers.usecases.PipelineControllerToStorageUseCase
import smarthome.client.entity.Controller

class PipelineControllerToStorageUseCaseImplTest {
    private lateinit var controllersRepo: ControllersRepo
    private lateinit var controllerToStorageUseCase: PipelineControllerToStorageUseCase
    
    @Before
    fun setUp() {
        controllersRepo = mock {}
        controllerToStorageUseCase = PipelineControllerToStorageUseCaseImpl(controllersRepo)
    }
    @Test
    fun `when execute should coordinate data to controllers repo`() {
        val controller = mock<Controller>()
        
        controllerToStorageUseCase.execute(controller)
        
        verify(controllersRepo).controllerUpdated(controller)
    }
}