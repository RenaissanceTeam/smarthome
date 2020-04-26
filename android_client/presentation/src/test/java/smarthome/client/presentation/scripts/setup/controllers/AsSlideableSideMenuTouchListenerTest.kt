package smarthome.client.presentation.scripts.setup.controllers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class AsSlideableSideMenuTouchListenerTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    
    private lateinit var touchListener: AsSlideableSideMenuTouchListener
    
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        
        touchListener = AsSlideableSideMenuTouchListener()
        touchListener.jumpTo.observeForever { }
    }
    
    @Test
    fun `when action down and move should emit delta`() {
        touchListener.setWidth(100f)
        touchListener.onActionDown(310f, 0f)
        touchListener.moveTo(320f)
        assertThat(touchListener.jumpTo.value).isEqualTo(10f)
    }
    
    @Test
    fun `should not move to left more than where it started`() {
        touchListener.setWidth(100f)
        touchListener.onActionDown(310f, 0f)
        touchListener.moveTo(309f)
        
        assertThat(touchListener.jumpTo.value).isEqualTo(0f)
    }
    
    @Test
    fun `moving right from out of bounds should be considered as moving from edge`() {
        touchListener.setWidth(100f)
        touchListener.onActionDown(310f, 0f)
        touchListener.moveTo(200f)
        assertThat(touchListener.jumpTo.value).isEqualTo(0f)
        
        touchListener.moveTo(210f)
        assertThat(touchListener.jumpTo.value).isEqualTo(10f)
    }
    
    @Test
    fun `moving left from out of bounds should be considered as moving from edge`() {
        touchListener.setWidth(100f)
        touchListener.onActionDown(310f, 0f)
        touchListener.moveTo(450f)
        assertThat(touchListener.jumpTo.value).isEqualTo(100f)
        
        touchListener.moveTo(440f)
        assertThat(touchListener.jumpTo.value).isEqualTo(90f)
    }
    
    @Test
    fun `moving should be recognized only if started on left side`() {
        touchListener.setWidth(100f)
        touchListener.onActionDown(310f, 0f)
        touchListener.moveTo(320f)
        assertThat(touchListener.jumpTo.value).isEqualTo(10f)
        
        touchListener.onActionDown(310f, 90f)
        touchListener.moveTo(311f)
        assertThat(touchListener.jumpTo.value).isEqualTo(10f) // value not changed
    }
    
    @Test
    fun `when action up and progress less than half should animate to open`() {
        touchListener.setWidth(100f)
        touchListener.onActionDown(310f, 0f)
        touchListener.moveTo(320f)
        touchListener.onActionUp()
        
        assertThat(touchListener.animateTo.value).isEqualTo(0f)
    }
    
    @Test
    fun `when action up and progress more than half should animate to close`() {
        touchListener.setWidth(100f)
        touchListener.onActionDown(300f, 0f)
        touchListener.moveTo(360f)
        touchListener.onActionUp()
        
        assertThat(touchListener.animateTo.value).isEqualTo(100f)
    }
}