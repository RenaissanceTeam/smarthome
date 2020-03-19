package smarthome.client.presentation.util.drag

import android.view.View
import android.view.ViewGroup
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.presentation.util.isPositionInside
import smarthome.client.presentation.util.rawPosition
import smarthome.client.util.Position

open class ViewGroupHost(private val viewGroup: ViewGroup) : DraggableHost, KoinComponent {
    override val hosted: MutableList<Draggable> = mutableListOf()
    private val draggableHostHolder by inject<DraggableHostHolder>()
    
    init {
        viewGroup.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) {
                draggableHostHolder.register(this@ViewGroupHost)
            }
            
            override fun onViewDetachedFromWindow(view: View) {
                viewGroup.removeOnAttachStateChangeListener(this)
                draggableHostHolder.unregister(this@ViewGroupHost)
            }
        })
    }
    
    override fun hitTest(position: Position): Boolean {
        return viewGroup.isPositionInside(position)
    }
    
    override fun convertRawToRelativePosition(raw: Position) = raw - viewGroup.rawPosition
    
    override fun onStartedDragging(draggable: Draggable) {}
    
    override fun onMovedDraggable(draggable: Draggable) {}
    
    override fun onFinishMovingDraggableInsideHost(draggable: Draggable) {}
    
    override fun onCancel(draggable: Draggable, initial: Position) {}
}