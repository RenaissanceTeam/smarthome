package smarthome.client.presentation.scripts.setup.graph.blockviews.dependency

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import smarthome.client.presentation.R
import smarthome.client.presentation.util.extensions.position
import smarthome.client.util.Position
import smarthome.client.util.emptyPosition
import kotlin.math.*
import kotlin.properties.Delegates

class DependencyArrowView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint()
    private val borderIntersectionHelper = BorderIntersectionHelper()

    init {
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 7f
        paint.isAntiAlias = true

        inflate(context, R.layout.scripts_dependency_arrow, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isNotInRange(event.position.x, startPosition.x, endPosition.x)
                || isNotInRange(event.position.y, startPosition.y, endPosition.y)
                || !onTheLine(event.position))
            return false

        return super.onTouchEvent(event)
    }

    fun setOnClick(listener: () -> Unit) {
        setOnClickListener { listener() }
    }

    private fun isNotInRange(toCheck: Int, a: Int, b: Int): Boolean {
        return !(toCheck > min(a, b) && toCheck < max(a, b))
    }

    private fun onTheLine(touchPosition: Position): Boolean {
        val x = (startPosition.y - endPosition.y) * touchPosition.x
        val y = (endPosition.x - startPosition.x) * touchPosition.y
        val c = (startPosition.x * endPosition.y) - (endPosition.x * startPosition.y)
        return abs(x + y + c) < 50000
    }

    private var startPosition: Position by Delegates.observable(emptyPosition) { _, old, new ->
        if (old == new) return@observable

        invalidate()
    }

    private var endPosition: Position by Delegates.observable(emptyPosition) { _, old, new ->
        if (old == new) return@observable

        invalidate()
    }


    fun setPositions(start: DependencyTip, end: DependencyTip) {
        val intersected = borderIntersectionHelper.tipPositionsWithIntersection(start, end)
        startPosition = intersected.first
        endPosition = intersected.second
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(
                startPosition.x.toFloat(),
                startPosition.y.toFloat(),
                endPosition.x.toFloat(),
                endPosition.y.toFloat(),
                paint
        )

        canvas.save()
        canvas.translate(endPosition.x.toFloat(), endPosition.y.toFloat())

        val angle = calculateDependencyAngle()
        canvas.rotate((angle * 180 / PI).toFloat())

        canvas.drawLine(0f, 0f, 10f, 30f, paint)
        canvas.drawLine(0f, 0f, -10f, 30f, paint)

        canvas.restore()
    }

    private fun calculateDependencyAngle(): Double {
        val delta = startPosition - endPosition

        if (delta.x == 0) return delta.y.sign.coerceIn(-1, 0) * PI
        if (delta.y == 0) return PI * delta.x.sign + PI / 2
        return atan(delta.y.toDouble() / delta.x) +
                PI * (delta.y.sign * delta.x.sign.coerceIn(0, 1)) +
                PI / 2
    }

}


