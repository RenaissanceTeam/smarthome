package smarthome.client.presentation.util

import android.view.MotionEvent
import androidx.lifecycle.MutableLiveData
import smarthome.client.util.Position

val MotionEvent.position get() = Position(
    x.toInt(), y.toInt())
val MotionEvent.rawPosition get() = Position(
    rawX.toInt(), rawY.toInt())
fun <T> MutableLiveData<T>.triggerRebuild() {
    this.value = value
}