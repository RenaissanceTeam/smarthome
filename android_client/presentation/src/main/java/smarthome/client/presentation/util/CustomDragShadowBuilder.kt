package smarthome.client.presentation.util

import android.graphics.Point
import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder


class CustomDragShadowBuilder(v: View, private val event: MotionEvent) : DragShadowBuilder(v) {
        override fun onProvideShadowMetrics(shadowSize: Point, shadowTouchPoint: Point) {
            shadowSize.set(view.width, view.height)
            shadowTouchPoint.set(event.x.toInt(), event.y.toInt())
        }
}