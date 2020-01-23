package smarthome.client.presentation.scripts.addition.graph.controllers

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.delay
import smarthome.client.presentation.util.KoinViewModel

class ControllersViewViewModel : KoinViewModel() {
    
    val isOpen = MutableLiveData(false)
    val dragging = MutableLiveData<Float>()
    private var currentSlide = 0f
    private var startSlide = 0f
    private var currentRelativeDragProgress = 0f
    private var width = 0f
    private var screenWidth = 0f
    private var waitingForMove = false
    
    fun onClick() {
        isOpen.value = false
    }
    
    fun open() {
        isOpen.value = true
    }
    
    fun onActionDown(at: Float, x: Float, width: Float): Boolean {
        if (x/width > LEFT_SIDE_PERCENT) {
            waitingForMove = false
            return false
        }
        
        this.width = width
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
    fun setScreenWidth(width: Float) {
        screenWidth = width
    }
    
    companion object {
        const val LEFT_SIDE_PERCENT = 0.3
    }
}
