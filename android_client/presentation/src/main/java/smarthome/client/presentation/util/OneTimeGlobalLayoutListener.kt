package smarthome.client.presentation.util

import android.view.ViewTreeObserver

class OneTimeGlobalLayoutListener(
    private val viewTreeObserver: ViewTreeObserver,
    private val callback: () -> Unit
) : ViewTreeObserver.OnGlobalLayoutListener {
    init {
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }
    
    override fun onGlobalLayout() {
        callback()
        viewTreeObserver.removeOnGlobalLayoutListener(this)
    }
}