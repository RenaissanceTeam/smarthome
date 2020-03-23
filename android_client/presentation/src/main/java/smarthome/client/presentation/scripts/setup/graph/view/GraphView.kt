package smarthome.client.presentation.scripts.setup.graph.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.scripts_graph.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.ext.scope
import smarthome.client.entity.script.block.BlockId
import smarthome.client.entity.script.dependency.DependencyId
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.setup.graph.blockviews.GraphBlockView
import smarthome.client.presentation.scripts.setup.graph.blockviews.dependency.*
import smarthome.client.presentation.scripts.setup.graph.blockviews.factory.GraphBlockFactoryResolver
import smarthome.client.presentation.scripts.setup.graph.blockviews.state.BlockState
import smarthome.client.presentation.scripts.setup.graph.events.drag.GraphDragEvent
import smarthome.client.presentation.util.doOnFirstLayout
import smarthome.client.presentation.util.drag.Draggable
import smarthome.client.presentation.util.drag.DraggableEvent
import smarthome.client.presentation.util.drag.ViewGroupHost
import smarthome.client.presentation.util.extensions.triggerRebuild
import smarthome.client.presentation.util.inflate
import smarthome.client.presentation.util.rawPosition
import smarthome.client.util.Position
import smarthome.client.util.visible

class GraphView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), KoinComponent {
    
    private var blockViews = mutableMapOf<BlockId, GraphBlockView>()
    private var dependencyViews = mutableMapOf<DependencyId, DependencyArrowView>()
    private var movingDependencyView = DependencyArrowView(context)
    private val viewModel by lazy { "setup".scope.get<GraphViewModel>() }
    private val graphBlockFactoryResolver: GraphBlockFactoryResolver by inject()
    private val dragHost = ViewGroupHost(this)
    private val movingDependencyProcessor by lazy {
        MovingDependencyProcessor(
            movingDependencyView,
            viewModel,
            { blockViews },
            { rawPosition },
            ::setStartToCenterOfBlock
        )
    }
    
    init {
        inflate(R.layout.scripts_graph)
    }
    
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        
        addView(movingDependencyView)
        handleDroppingBlocksOntoGraph()
        ViewTreeLifecycleOwner.get(this)?.let(::observeViewModel)
    }
    
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        
        setOnDragListener(null)
    }
    
    private fun observeViewModel(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(viewModel)
    
        viewModel.blocks.observe(lifecycleOwner, this::bindBlocks)
        viewModel.dependencies.observe(lifecycleOwner, this::bindDependencies)
        viewModel.movingDependency.observe(lifecycleOwner, movingDependencyProcessor::onData)
    }
    
    private fun setStartToCenterOfBlock(view: DependencyArrowView?, startBlock: BlockId?) {
        startBlock?.let { startId ->
            val start = runCatching { getOrInflateBlockView(startId) }.getOrElse { return@let }
            view?.setStart(start.centerPosition)
        }
    }
    
    private fun bindBlocks(blocks: List<BlockState>) {
        retainOnlyPostedBlocks(blocks)
        
        blocks.forEach { block ->
            getOrInflateBlockView(block).setData(block)
        }
    }
    
    
    private fun retainOnlyPostedBlocks(blocks: List<BlockState>) {
        (blockViews.keys - blocks.map { it.block.id }).forEach {
            (blockViews.remove(it)).let(this::removeView)
        }
    }
    
    private fun retainOnlyPostedDependencies(dependencies: List<DependencyState>) {
        (dependencyViews.keys - dependencies.map { it.dependency.id })
            .forEach { (dependencyViews.remove(it)).let(this::removeView) }
    }
    
    private fun getOrInflateBlockView(blockState: BlockState): GraphBlockView {
        return blockViews[blockState.block.id]
            ?: inflateBlockView(blockState).also { blockViews[blockState.block.id] = it }
    }
    
    private fun inflateBlockView(blockState: BlockState): GraphBlockView {
        return graphBlockFactoryResolver.resolve(blockState).inflate(graph, blockState).apply {
            doOnFirstLayout {
                viewModel.dependencies.triggerRebuild()
                
                draggable?.let { draggable ->
                    dragHost.onAdd(draggable)
                    notifyViewModelOfDragEvents(draggable, blockState)
                }
            }
        }
    }
    
    @SuppressLint("CheckResult")
    private fun notifyViewModelOfDragEvents(draggable: Draggable, blockState: BlockState) {
        draggable.observeEvents().subscribe {
            if (it != DraggableEvent.MOVE) return@subscribe
            
            draggable.currentHostPosition?.let { hostPosition ->
                viewModel.onBlockMoved(blockState.block.id, hostPosition)
            }
        }
    }
    
    private fun getOrInflateBlockView(blockId: BlockId): GraphBlockView {
        return blockViews[blockId]
            ?: {
                val state = viewModel.getBlockState(blockId)
                    ?: throw IllegalArgumentException("can't get block state for id = $blockId")
                getOrInflateBlockView(state)
            }.invoke()
    }
    
    private fun bindDependencies(dependencies: List<DependencyState>) {
        retainOnlyPostedDependencies(dependencies)
        
        dependencies.forEach { state ->
            val view = getOrInflateDependency(state.dependency.id)
            
            setStartToCenterOfBlock(view, state.dependency.startBlock)
            setEndToCenterOfBlock(view, state.dependency.endBlock)
        }
    }
    
    private fun setEndToCenterOfBlock(view: DependencyArrowView, id: BlockId) {
        val endBlock = runCatching { getOrInflateBlockView(id) }.getOrElse { return }
        endBlock.centerPosition.let(view::setEnd)
    }
    
    
    private fun getOrInflateDependency(id: DependencyId): DependencyArrowView {
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
}


private class MovingDependencyProcessor(
    private val movingDependencyView: DependencyArrowView,
    private val viewModel: GraphViewModel,
    private val blockViews: () -> Map<BlockId, GraphBlockView>,
    private val graphRawPosition: () -> Position,
    private val setStartToCenterOfBlock: (DependencyArrowView, BlockId?) -> Unit
) {
    
    fun onData(dependency: MovingDependency) {
        
        when (dependency.status) {
            IDLE -> {
                movingDependencyView.visible = false
            }
            STARTED -> {
                movingDependencyView.visible = true
                setMovingDependencyEnd(dependency)
                setStartToCenterOfBlock(movingDependencyView, dependency.startBlock)
            }
            MOVING -> {
                setMovingDependencyEnd(dependency)
                highlightBlockOnDependencyTip(dependency)
            }
            DROPPED -> {
                addOrCancelDependency(dependency)
            }
        }
    }
    
    
    private fun setMovingDependencyEnd(dependency: MovingDependency) {
        dependency.rawEndPosition
            ?.let(::convertRawToRelativePosition)
            ?.let(movingDependencyView::setEnd)
    }
    
    private fun addOrCancelDependency(dependency: MovingDependency) {
        if (dependency.rawEndPosition == null
            || dependency.id == null
            || dependency.startBlock == null) return
        
        val droppedTo = findBlockOnDependencyTip(dependency.rawEndPosition)
        
        when (droppedTo == null) {
            true -> viewModel.cancelCreatingDependency()
            false -> viewModel.addDependency(
                dependency.id,
                dependency.startBlock,
                droppedTo.key
            )
        }
    }
    
    private fun highlightBlockOnDependencyTip(dependency: MovingDependency) {
        if (dependency.rawEndPosition == null) return
        
        val movedTo = findBlockOnDependencyTip(dependency.rawEndPosition)
        when (movedTo == null) {
            true -> viewModel.dependencyTipNotOnAnyBlock()
            false -> dependency.startBlock?.let {
                viewModel.dependencyTipOnBlock(it, movedTo.key)
            }
        }
    }
    
    private fun findBlockOnDependencyTip(rawPosition: Position): Map.Entry<BlockId, GraphBlockView>? {
        return blockViews().asIterable().find { entry ->
            val block = entry.value
            
            val tipPosition = convertRawToRelativePosition(rawPosition)
            block.contains(tipPosition)
        }
    }
    
    private fun convertRawToRelativePosition(raw: Position): Position {
        return raw - graphRawPosition()
    }
}