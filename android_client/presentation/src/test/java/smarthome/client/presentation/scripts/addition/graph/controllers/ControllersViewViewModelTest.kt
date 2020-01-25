package smarthome.client.presentation.scripts.addition.graph.controllers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import smarthome.client.domain.api.conrollers.usecases.ObserveControllerUseCase
import smarthome.client.domain.api.devices.dto.GeneralDeviceInfo
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
        viewModel.jumpTo.observeForever { }
    }
    
    @After
    fun tearDown() {
        stopKoin()
    }
    
    @Test
    fun `when action down and move should emit delta`() {
        viewModel.setWidth(100f)
        viewModel.onActionDown(310f, 0f)
        viewModel.moveTo(320f)
        assertThat(viewModel.jumpTo.value).isEqualTo(10f)
    }
    
    @Test
    fun `should not move to left more than where it started`() {
        viewModel.setWidth(100f)
        viewModel.onActionDown(310f, 0f)
        viewModel.moveTo(309f)
    
        assertThat(viewModel.jumpTo.value).isEqualTo(0f)
    }
    
    @Test
    fun `moving right from out of bounds should be considered as moving from edge`() {
        viewModel.setWidth(100f)
        viewModel.onActionDown(310f, 0f)
        viewModel.moveTo(200f)
        assertThat(viewModel.jumpTo.value).isEqualTo(0f)

        viewModel.moveTo(210f)
        assertThat(viewModel.jumpTo.value).isEqualTo(10f)
    }
    
    @Test
    fun `moving left from out of bounds should be considered as moving from edge`() {
        viewModel.setWidth(100f)
        viewModel.onActionDown(310f, 0f)
        viewModel.moveTo(450f)
        assertThat(viewModel.jumpTo.value).isEqualTo(100f)
    
        viewModel.moveTo(440f)
        assertThat(viewModel.jumpTo.value).isEqualTo(90f)
    }
    
    @Test
    fun `moving should be recognized only if started on left side`() {
        viewModel.setWidth(100f)
        viewModel.onActionDown(310f, 0f)
        viewModel.moveTo(320f)
        assertThat(viewModel.jumpTo.value).isEqualTo(10f)
    
        viewModel.onActionDown(310f, 90f)
        viewModel.moveTo(311f)
        assertThat(viewModel.jumpTo.value).isEqualTo(10f) // value not changed
    }
    
    @Test
    fun `when action up and progress less than half should animate to open`() {
        viewModel.setWidth(100f)
        viewModel.onActionDown(310f, 0f)
        viewModel.moveTo(320f)
        viewModel.onActionUp()
        
        assertThat(viewModel.animateTo.value).isEqualTo(0f)
    }
    
    @Test
    fun `when action up and progress more than half should animate to close`() {
        viewModel.setWidth(100f)
        viewModel.onActionDown(300f, 0f)
        viewModel.moveTo(360f)
        viewModel.onActionUp()
    
        assertThat(viewModel.animateTo.value).isEqualTo(100f)
    }
    
    @Test
    fun `when controller is dragged to graph should update map of dragged controllers`() {
        viewModel.onDraggedToGraph(2)
        
        assertThat(viewModel.shouldShow(2)).isEqualTo(false)
        assertThat(viewModel.shouldShow(1)).isEqualTo(true)
    }
}