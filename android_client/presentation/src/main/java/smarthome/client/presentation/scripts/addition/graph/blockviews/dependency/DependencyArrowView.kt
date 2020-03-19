package smarthome.client.presentation.scripts.addition.graph.blockviews.dependency

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import smarthome.client.util.Position
import smarthome.client.presentation.R
import smarthome.client.util.emptyPosition
import kotlin.properties.Delegates


class DependencyArrowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint()
    init {
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4f
        paint.isAntiAlias = true
    
        inflate(context, R.layout.scripts_dependency_arrow, null)
    }
    
    private var startPosition: Position by Delegates.observable(emptyPosition) { _, old, new ->
        if (old == new) return@observable
        
        invalidate()
    }
    
    private var endPosition: Position by Delegates.observable(emptyPosition) { _, old, new ->
        if (old == new) return@observable
        
        invalidate()
    }
    
    fun setStart(position: Position) {
        startPosition = position
    }
    
    fun setEnd(position: Position) {
        endPosition = position
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
    }
}

