package smarthome.client.presentation.util.drag

import android.view.View
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.presentation.util.setPosition
import smarthome.client.util.Position
import smarthome.client.util.emptyPosition

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