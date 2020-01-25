package smarthome.client.presentation.scripts.addition.graph

import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.widget.FrameLayout
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.scripts_graph.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.presentation.R
import smarthome.client.presentation.util.inflate
import smarthome.client.presentation.util.lifecycleOwner

class ScriptGraphView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), KoinComponent {
    
    init {
        inflate(R.layout.scripts_graph)
    }
    
    private var blockViews = mutableMapOf<Long, GraphDraggable>()
    private val viewModel = ScriptGraphViewModel()
    private val graphBlockFactoryResolver: GraphBlockFactoryResolver by inject()
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    
        handleDroppingBlocksOntoGraph()
        
        val lifecycle = lifecycleOwner ?: return
        
        viewModel.blocks.observe(lifecycle) { blocks ->
            blocks.forEach { block ->
                var blockView = blockViews[block.id]
                
                if (blockView == null) {
                    val viewFactory = graphBlockFactoryResolver.resolve(block) ?: return@forEach
                    blockView = viewFactory.inflate(graph, block)
                    blockViews[block.id] = blockView
                }
                
                blockView.moveTo(block.position)
            }
        }
    }
    
    private fun handleDroppingBlocksOntoGraph() {
        setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    val draggable =
                        event.localState as? GraphDraggable ?: return@setOnDragListener false
                
                    draggable.onDraggedToGraph()
                    viewModel.onDropped(draggable, Position(event.x, event.y)) // todo add shift with draggable.touchPosition
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
