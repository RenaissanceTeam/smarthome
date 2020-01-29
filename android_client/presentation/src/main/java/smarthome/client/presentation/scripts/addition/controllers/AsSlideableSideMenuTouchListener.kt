package smarthome.client.presentation.scripts.addition.controllers

import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.MutableLiveData

fun setupSlideableMenu(view: View, block: AsSlideableSideMenuTouchListener.() -> Unit)
    : AsSlideableSideMenuTouchListener {
    
    return AsSlideableSideMenuTouchListener().apply {
        view.setOnTouchListener(this)
        block.invoke(this)
    }
}

class AsSlideableSideMenuTouchListener : View.OnTouchListener {
    val jumpTo = MutableLiveData<Float>()
    val animateTo = MutableLiveData<Float>()
    private var currentSlide = 0f
    private var startSlide = 0f
    private var currentRelativeDragProgress = 0f
    private var width = 0f
    private var waitingForMove = false
    
    fun toDefaultPosition() {
        jumpTo.value = width
    }
    
    fun setWidth(width: Float) {
        this.width = width
    }
    
    fun open() {
        animateTo.value = 0f
    }
    
    fun onActionDown(at: Float, x: Float): Boolean {
        if (x / width > ControllersHubViewModel.LEFT_SIDE_PERCENT) {
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
        jumpTo.value = currentRelativeDragProgress
        return true
    }
    
    fun onActionUp(): Boolean {
        if (!waitingForMove) return false
        
        when (currentRelativeDragProgress / width > 0.5) {
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
    
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onActionDown(event.rawX, event.x)
            }
            MotionEvent.ACTION_MOVE -> {
                moveTo(event.rawX)
            }
            MotionEvent.ACTION_UP -> {
                onActionUp()
            }
            else -> {
                false
            }
        }
    }
}
