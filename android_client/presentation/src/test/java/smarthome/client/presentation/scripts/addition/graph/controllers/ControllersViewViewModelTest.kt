package smarthome.client.presentation.scripts.addition.graph.controllers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ControllersViewViewModelTest {
    
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    
    private lateinit var viewModel: ControllersViewViewModel
    
    @Before
    fun setUp() {
        viewModel = ControllersViewViewModel()
        viewModel.dragging.observeForever { }
    }
    
    @Test
    fun `when action down and move should emit delta`() {
        viewModel.onActionDown(310f, 0f, 100f)
        viewModel.moveTo(320f)
        assertThat(viewModel.dragging.value).isEqualTo(10f)
    }
    
    @Test
    fun `should not move to left more than where it started`() {
        viewModel.onActionDown(310f, 0f, 100f)
        viewModel.moveTo(309f)
    
        assertThat(viewModel.dragging.value).isEqualTo(0f)
    }
    
    @Test
    fun `moving right from out of bounds should be considered as moving from edge`() {
        viewModel.onActionDown(310f, 0f, 100f)
        viewModel.moveTo(200f)
        assertThat(viewModel.dragging.value).isEqualTo(0f)

        viewModel.moveTo(210f)
        assertThat(viewModel.dragging.value).isEqualTo(10f)
    }
    
    @Test
    fun `moving left from out of bounds should be considered as moving from edge`() {
        viewModel.onActionDown(310f, 0f, 100f)
        viewModel.moveTo(450f)
        assertThat(viewModel.dragging.value).isEqualTo(100f)
    
        viewModel.moveTo(440f)
        assertThat(viewModel.dragging.value).isEqualTo(90f)
    }
    
    @Test
    fun `moving should be recognized only if started on left side`() {
        viewModel.onActionDown(310f, 0f, 100f)
        viewModel.moveTo(320f)
        assertThat(viewModel.dragging.value).isEqualTo(10f)
    
        viewModel.onActionDown(310f, 90f, 100f)
        viewModel.moveTo(311f)
        assertThat(viewModel.dragging.value).isEqualTo(10f) // value not changed
    }
}