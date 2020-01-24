package smarthome.client.presentation.scripts.addition.graph

import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.widget.FrameLayout
import smarthome.client.presentation.R
import smarthome.client.presentation.util.inflate
import smarthome.client.util.log

class ScriptGraphView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    
    init {
        inflate(R.layout.scripts_graph)
    }
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        
        setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    val draggable =
                        event.localState as? GraphDraggable ?: return@setOnDragListener false
                    draggable.onDraggedToGraph()
                }
            }
            true
        }
    }
    
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        
        setOnDragListener(null)
    }
}