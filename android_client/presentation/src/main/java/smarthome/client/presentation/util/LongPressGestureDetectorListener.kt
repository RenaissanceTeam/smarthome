package smarthome.client.presentation.util

import android.view.GestureDetector
import android.view.MotionEvent

class LongPressGestureDetectorListener : GestureDetector.SimpleOnGestureListener() {
    private var onLongPress: ((e: MotionEvent) -> Boolean)? = null
    
    override fun onLongPress(e: MotionEvent) {
        onLongPress?.invoke(e)
    }
    
    fun onLongPressListener(listener: (e: MotionEvent) -> Boolean) {
        onLongPress = listener
    }
}