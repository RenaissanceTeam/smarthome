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
import smarthome.client.presentation.scripts.addition.graph.events.drag.GraphDragEvent
import smarthome.client.presentation.scripts.addition.graph.identifier.GraphBlockIdentifier
import smarthome.client.presentation.scripts.addition.graph.blockviews.GraphBlockView
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlock
import smarthome.client.presentation.scripts.addition.graph.blockviews.factory.GraphBlockFactoryResolver
import smarthome.client.presentation.util.Position
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
    
    private var blockViews = mutableMapOf<GraphBlockIdentifier, GraphBlockView>()
    private val viewModel = ScriptGraphViewModel()
    private val graphBlockFactoryResolver: GraphBlockFactoryResolver by inject()
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    
        handleDroppingBlocksOntoGraph()
        
        val lifecycle = lifecycleOwner ?: return
        
        viewModel.blocks.observe(lifecycle) { blocks ->
            blocks.values.forEach { block ->
                getOrInflateBlockView(block).setData(block)
            }
    
            retainOnlyPostedBlocks(blocks)
        }
    }
    
    private fun retainOnlyPostedBlocks(blocks: MutableMap<GraphBlockIdentifier, GraphBlock>) {
        (blockViews.keys - blocks.keys).forEach { blockViews.remove(it) }
    }
    
    private fun getOrInflateBlockView(block: GraphBlock): GraphBlockView {
        var blockView = blockViews[block.id]
        
        if (blockView == null) {
            val viewFactory = graphBlockFactoryResolver.resolve(block)
            blockView = viewFactory.inflate(graph, block)
            blockViews[block.id] = blockView
        }
        
        return blockView
    }
    
    private fun handleDroppingBlocksOntoGraph() {
        setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    val dragInfo = event.localState as? GraphDragEvent ?: return@setOnDragListener false
                    viewModel.onDropped(dragInfo,
                        Position(event.x,
                            event.y))
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
