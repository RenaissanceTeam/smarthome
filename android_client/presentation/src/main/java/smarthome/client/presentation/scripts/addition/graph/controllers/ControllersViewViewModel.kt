package smarthome.client.presentation.scripts.addition.graph.controllers

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.delay
import smarthome.client.presentation.util.KoinViewModel
import kotlin.math.round

class ControllersViewViewModel : KoinViewModel() {
    
    val dragging = MutableLiveData<Float>()
    val animateTo = MutableLiveData<Float>()
    val jumpTo = MutableLiveData<Float>()
    
    private var currentSlide = 0f
    private var startSlide = 0f
    private var currentRelativeDragProgress = 0f
    private var width = 0f
    private var waitingForMove = false
    
    fun open() {
        animateTo.value = 0f
    }
    
    fun onActionDown(at: Float, x: Float): Boolean {
        if (x/width > LEFT_SIDE_PERCENT) {
            waitingForMove = false
            return false
        }
        
        waitingForMove = true
        startSlide = at
        currentSlide = at
        currentRelativeDragProgress = 0f
        return true
    }
    
    fun moveTo(newX: Float): Boolean {
        if (!waitingForMove) return false
        
        val delta = newX - currentSlide
        currentSlide = newX
        
        currentRelativeDragProgress = minOf(maxOf(currentRelativeDragProgress + delta, 0f), width)
        dragging.value = currentRelativeDragProgress
        return true
    }
    
    fun setWidth(width: Float) {
        this.width = width
    }
    
    fun onActionUp(): Boolean {
        if (!waitingForMove) return false
    
        when (currentRelativeDragProgress/width > 0.5) {
            true -> animateToClose()
            false -> animateToOpen()
        }
        return true
    }
    
    private fun animateToOpen() {
        animateTo.value = 0f
    }
    
    private fun animateToClose() {
        animateTo.value = width
    }
    
    fun onInit() {
        jumpTo.value = width
    }
    
    companion object {
        const val LEFT_SIDE_PERCENT = 0.3
    }
}
