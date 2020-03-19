package smarthome.client.presentation.util

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.presentation.util.extensions.rawPosition
import smarthome.client.util.Position
import smarthome.client.util.emptyPosition
import smarthome.client.util.log
import smarthome.client.util.toPosition

open class DraggableView(private val view: View, private val touchHandler: View) : Draggable, KoinComponent {
    private val draggableHostHolder by inject<DraggableHostHolder>()
    
    override var host: DraggableHost? = null
    override var possibleHosts: () -> List<DraggableHost> = draggableHostHolder::get
    override var stableRawPosition = emptyPosition
    override var currentRawPosition = emptyPosition
    override var touchPosition = emptyPosition
    
    init {
        setTouchHandler(touchHandler)
    }
    
    override fun doUiMove(position: Position) {
        view.setPosition(position)
    }
}
interface Draggable {
    
    var host: DraggableHost?
    var possibleHosts: () -> List<DraggableHost>
    var stableRawPosition: Position
    var currentRawPosition: Position
    var touchPosition: Position
    
    private fun setCurrentPosition(position: Position) {
        currentRawPosition = position - touchPosition
    }
    
    fun setTouchHandler(view: View) {
        view.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val touchX = (view.x + event.x).toInt()
                    val touchY = (view.y + event.y).toInt()
                    touchPosition = Position(touchX, touchY)
    
    
                    setCurrentPosition(event.rawPosition)
                    host?.onStartedDragging(this)
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    setCurrentPosition(event.rawPosition)
    
                    host?.onMovedDraggable(this)
                    true
                }
                MotionEvent.ACTION_UP -> {
                    when (val newHost = possibleHosts().find { it.hitTest(currentRawPosition) }) {
                        null -> {
                            currentRawPosition = stableRawPosition
                            host?.onCancel(this, stableRawPosition)
                        }
                        host -> {
                            stableRawPosition = currentRawPosition
                            host?.onFinishMovingDraggableInsideHost(this)
                        }
                        else -> {
                            stableRawPosition = currentRawPosition
                            host?.onRemove(this)
                            newHost.onAdd(this)
                        }
                    }
                    true
                }
                else -> false
            }
            
        }
    }
    
    fun doUiMove(position: Position)
}

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
    
    override fun onStartedDragging(draggable: Draggable) {
    
    }
    
    override fun onMovedDraggable(draggable: Draggable) {
        val relative = convertRawToRelativePosition(draggable.currentRawPosition)
        draggable.doUiMove(relative)
    }
    
    private fun convertRawToRelativePosition(raw: Position): Position {
        val graphPosition = IntArray(2).also { viewGroup.getLocationOnScreen(it) }.toPosition()
        
        val relative = raw - graphPosition
        
        log("raw=$raw, relative=$relative")
        return relative
    }
    
    override fun onFinishMovingDraggableInsideHost(draggable: Draggable) {}
    
    override fun onCancel(draggable: Draggable, initial: Position) {
        val relative = convertRawToRelativePosition(initial)
        draggable.doUiMove(relative)
    }
}

interface DraggableHost {
    val hosted: MutableList<Draggable>
    
    fun hitTest(position: Position): Boolean
    fun onStartedDragging(draggable: Draggable)
    fun onMovedDraggable(draggable: Draggable)
    fun onFinishMovingDraggableInsideHost(draggable: Draggable)
    
    fun onCancel(draggable: Draggable, initial: Position)
    
    fun onAdd(draggable: Draggable) {
        hosted.add(draggable)
        draggable.host = this
    }
    
    fun onRemove(draggable: Draggable) {
        hosted.remove(draggable)
        draggable.host = null
    }
}

interface DraggableHostHolder {
    fun register(host: DraggableHost)
    fun unregister(host: DraggableHost)
    fun get(): List<DraggableHost>
}

class DraggableHostHolderImpl : DraggableHostHolder {
    private val hosts = mutableListOf<DraggableHost>()
    
    override fun register(host: DraggableHost) {
        hosts.add(host)
    }
    
    override fun unregister(host: DraggableHost) {
        hosts.remove(host)
    }
    
    override fun get(): List<DraggableHost> {
        return hosts
    }
}