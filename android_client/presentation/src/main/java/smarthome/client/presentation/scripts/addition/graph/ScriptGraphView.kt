package smarthome.client.presentation.scripts.addition.graph

import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.scripts_graph.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.addition.graph.blockviews.GraphBlockView
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.DependencyArrowView
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.DependencyState
import smarthome.client.presentation.scripts.addition.graph.blockviews.factory.GraphBlockFactoryResolver
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.GraphBlock
import smarthome.client.presentation.scripts.addition.graph.events.drag.GraphDragEvent
import smarthome.client.presentation.scripts.addition.graph.identifier.GraphBlockIdentifier
import smarthome.client.presentation.util.Position
import smarthome.client.presentation.util.inflate
import smarthome.client.presentation.util.lifecycleOwner
import smarthome.client.presentation.util.toPosition

class ScriptGraphView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), KoinComponent {
    
    init {
        inflate(R.layout.scripts_graph)
    }
    
    private var blockViews = mutableMapOf<GraphBlockIdentifier, GraphBlockView>()
    private var dependencyViews = mutableMapOf<String, DependencyArrowView>()
    private val viewModel = ScriptGraphViewModel()
    private val graphBlockFactoryResolver: GraphBlockFactoryResolver by inject()
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    
        handleDroppingBlocksOntoGraph()
        lifecycleOwner?.let(::observeViewModel)
    }
    
    private fun observeViewModel(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(viewModel)
        
        viewModel.blocks.observe(lifecycleOwner, this::bindBlocks)
        viewModel.dependencies.observe(lifecycleOwner, this::bindDependencies)
        viewModel.movingDependencyTip.observe(lifecycleOwner) { status ->
            if (status.isMoving) findBlockOnDependencyTip(status.from, status.rawPosition)
        }
    }
    
    private fun bindBlocks(blocks: MutableMap<GraphBlockIdentifier, GraphBlock>) {
        blocks.values.forEach { block ->
            getOrInflateBlockView(block).setData(block)
        }
        retainOnlyPostedBlocks(blocks)
    }
    
    private fun findBlockOnDependencyTip(from: GraphBlockIdentifier, rawPosition: Position) {
        val block = blockViews.asIterable().find { entry ->
            val block = entry.value
            
            val tipPosition = convertRawToRelativePosition(rawPosition)
            block.contains(tipPosition)
        }
        
        when (block == null) {
            true -> viewModel.dependencyTipNotOnAnyBlock()
            false -> viewModel.dependencyTipOnBlock(from, block.key)
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
    
    private fun bindDependencies(dependencies: MutableMap<String, DependencyState>) {
        dependencies.values.forEach { dependency ->
            val view = getOrInflateDependency(dependency.id)
            
            val startBlock = dependency.startBlock?.let { blockViews[it] }
            startBlock?.centerPosition?.let(view::setStart)
            
            dependency.rawEndPosition?.let { endPosition ->
                view.setEnd(convertRawToRelativePosition(endPosition))
            }
        }
    }
    
    private fun convertRawToRelativePosition(raw: Position): Position {
        val graphPosition = IntArray(2).also { getLocationOnScreen(it) }.toPosition()
        
        return raw - graphPosition
    }
    
    private fun getOrInflateDependency(id: String): DependencyArrowView {
        return dependencyViews[id]
            ?: DependencyArrowView(context).also(this::addView).also { dependencyViews[id] = it }
    }
    
    private fun handleDroppingBlocksOntoGraph() {
        setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    val dragInfo = event.localState as? GraphDragEvent ?: return@setOnDragListener false
                    viewModel.onDropped(dragInfo, Position(event.x, event.y))
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
