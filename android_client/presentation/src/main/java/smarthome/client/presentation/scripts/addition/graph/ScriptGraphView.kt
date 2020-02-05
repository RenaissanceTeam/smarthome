package smarthome.client.presentation.scripts.addition.graph

import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.scripts_graph.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import smarthome.client.entity.script.BlockId
import smarthome.client.entity.script.Position
import smarthome.client.entity.script.toPosition
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.addition.graph.blockviews.GraphBlockView
import smarthome.client.presentation.scripts.addition.graph.blockviews.dependency.*
import smarthome.client.presentation.scripts.addition.graph.blockviews.factory.GraphBlockFactoryResolver
import smarthome.client.presentation.scripts.addition.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.addition.graph.events.drag.GraphDragEvent
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
    
    private var blockViews = mutableMapOf<BlockId, GraphBlockView>()
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
        viewModel.movingDependencyTip.observe(lifecycleOwner) { tip ->
            when (tip.status) {
                IDLE -> {
                }
                MOVING -> {
                    val movedTo = findBlockOnDependencyTip(tip.rawPosition)
                
                    when (movedTo == null) {
                        false -> tip.from?.let { viewModel.dependencyTipOnBlock(it, movedTo.key) }
                        true -> viewModel.dependencyTipNotOnAnyBlock()
                    }
                }
                DROPPED -> {
                    val droppedTo = findBlockOnDependencyTip(tip.rawPosition)
                    if (tip.dependencyId == null) return@observe
                    when (droppedTo == null) {
                        false -> if (tip.from != null) {
                            viewModel.addDependency(tip.dependencyId, tip.from, droppedTo.key)
                        }
                        true -> viewModel.cancelCreatingDependency(tip.dependencyId)
                    }
                }
            }
        }
    }
    
    private fun bindBlocks(blocks: List<BlockState>) {
        retainOnlyPostedBlocks(blocks)
        
        blocks.forEach { block ->
            getOrInflateBlockView(block).setData(block)
        }
    }
    
    private fun findBlockOnDependencyTip(
        rawPosition: Position): Map.Entry<BlockId, GraphBlockView>? {
        return blockViews.asIterable().find { entry ->
            val block = entry.value
            
            val tipPosition = convertRawToRelativePosition(rawPosition)
            block.contains(tipPosition)
        }
    }
    
    private fun retainOnlyPostedBlocks(blocks: List<BlockState>) {
        (blockViews.keys - blocks.map { it.block.id }).forEach {
            (blockViews.remove(it) as? View)?.let(this::removeView)
        }
    }
    
    private fun retainOnlyPostedDependencies(dependencies: MutableMap<String, DependencyState>) {
        (dependencyViews.keys - dependencies.keys).forEach {
            (dependencyViews.remove(it) as? View)?.let(this::removeView)
        }
    }
    
    private fun getOrInflateBlockView(blockState: BlockState): GraphBlockView {
        var blockView = blockViews[blockState.block.id]
        
        if (blockView == null) {
            val viewFactory = graphBlockFactoryResolver.resolve(blockState)
            blockView = viewFactory.inflate(graph, blockState)
            blockViews[blockState.block.id] = blockView
        }
        
        return blockView
    }
    
    private fun bindDependencies(dependencies: MutableMap<String, DependencyState>) {
        retainOnlyPostedDependencies(dependencies)
        
        dependencies.values.forEach { dependency ->
            val view = getOrInflateDependency(dependency.id)
            
            val startBlock = dependency.startBlock?.let { blockViews[it] }
            startBlock?.centerPosition?.let(view::setStart)
            
            val endBlock = dependency.endBlock?.let { blockViews[it] }
            when (endBlock != null) {
                true -> endBlock.centerPosition.let(view::setEnd)
                false -> {
                    dependency.rawEndPosition?.let { endPosition ->
                        view.setEnd(convertRawToRelativePosition(endPosition))
                    }
                }
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
            val dragInfo = event.localState as? GraphDragEvent ?: return@setOnDragListener false
            
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> true
                DragEvent.ACTION_DRAG_ENDED -> {
                    if (!event.result) viewModel.onCanceled(dragInfo)
                    true
                }
                DragEvent.ACTION_DROP -> {
                    viewModel.onDropped(dragInfo, Position(event.x.toInt(), event.y.toInt()))
                    true
                }
                else -> false
            }
        }
    }
    
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        
        setOnDragListener(null)
    }
}
