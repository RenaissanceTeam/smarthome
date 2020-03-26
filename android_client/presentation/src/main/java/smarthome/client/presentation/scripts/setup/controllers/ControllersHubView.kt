package smarthome.client.presentation.scripts.setup.controllers

import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.scripts_controllers_to_add.view.*
import org.koin.core.KoinComponent
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.setup.controllers.epoxy.DevicesController
import smarthome.client.presentation.scripts.setup.di.setupScope
import smarthome.client.presentation.scripts.setup.graph.events.drag.BlockDragEvent
import smarthome.client.presentation.util.inflate

class ControllersHubView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), KoinComponent {
    
    private val viewModel by lazy { setupScope.get<ControllersHubViewModel>() }
    private val itemsController = DevicesController()
    private var onOpenMenuCallback: () -> Unit = {}
    
    init {
        inflate(R.layout.scripts_controllers_to_add)
    
        devices.layoutManager = LinearLayoutManager(context)
        devices.adapter = itemsController.adapter
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        
        ViewTreeLifecycleOwner.get(this)?.let { lifecycleOwner ->
            lifecycleOwner.lifecycle.addObserver(viewModel)
    
            setupSlideableMenu(lifecycleOwner)
    
            viewModel.devices.observe(lifecycleOwner) { itemsController.setData(it, viewModel) }
            handleDroppedItemsAsCancelledDragAction()
        }
    }
    
    private fun setupSlideableMenu(lifecycle: LifecycleOwner) {
        setupSlideableMenu(this) {
            jumpTo.distinctUntilChanged().observe(lifecycle) { animate().translationX(it).duration = 0 }
            animateTo.observe(lifecycle) { animate().translationX(it).duration = 300 }
        
            onLayoutReady(this)
            onOpenMenuCallback = this::open
        }
    }
    
    private fun onLayoutReady(slideableController: AsSlideableSideMenuTouchListener) {
        post {
            slideableController.setWidth(width.toFloat())
            slideableController.toDefaultPosition()
        }
    }
    
    private fun handleDroppedItemsAsCancelledDragAction() {
        setOnDragListener { _, event ->
            val dragInfo = event.localState as? BlockDragEvent ?: return@setOnDragListener false
            
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> true
                DragEvent.ACTION_DROP -> {
                    viewModel.onDropped(dragInfo)
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
    
    fun open() {
        onOpenMenuCallback()
        viewModel.open()
    }
}

