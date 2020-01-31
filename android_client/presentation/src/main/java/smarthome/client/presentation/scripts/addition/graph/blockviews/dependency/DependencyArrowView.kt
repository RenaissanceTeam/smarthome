package smarthome.client.presentation.scripts.addition.graph.blockviews.dependency

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.epoxy.*
import smarthome.client.presentation.R
import smarthome.client.presentation.util.Position
import smarthome.client.presentation.util.inflate

class DependencyArrowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    init {
        inflate(R.layout.scripts_dependency_arrow)
    }
    
    fun setStart(position: Position) {
    
    }
    
    fun setEnd(position: Position) {
    
    }
}

