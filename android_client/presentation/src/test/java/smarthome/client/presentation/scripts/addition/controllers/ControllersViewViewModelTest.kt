package smarthome.client.presentation.scripts.addition.controllers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.domain.api.devices.usecase.GetGeneralDevicesInfo

class ControllersViewViewModelTest {
    
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    
    private lateinit var viewModel: ControllersViewViewModel
    private lateinit var getDevicesUseCase: GetGeneralDevicesInfo
    private lateinit var observeControllerUseCase: ObserveControllerUseCase
    
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        getDevicesUseCase = mock { }
        observeControllerUseCase = mock { }
    
        startKoin {
            modules(module {
                single { getDevicesUseCase }
                single { observeControllerUseCase }
            })
        }
        viewModel = ControllersViewViewModel()
    }
    
    @After
    fun tearDown() {
        stopKoin()
    }

//    @Test
//    fun `when controller is dragged to graph should update map of dragged controllers`() {
//        viewModel.onDraggedToGraph(2)
//
//        assertThat(viewModel.shouldShow(2)).isEqualTo(false)
//        assertThat(viewModel.shouldShow(1)).isEqualTo(true)
//    }
}