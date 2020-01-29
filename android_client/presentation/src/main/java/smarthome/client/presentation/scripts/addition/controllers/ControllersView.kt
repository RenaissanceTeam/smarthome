package smarthome.client.presentation.scripts.addition.controllers

import android.content.Context
import android.util.AttributeSet
import android.view.DragEvent
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.snakydesign.livedataextensions.distinct
import kotlinx.android.synthetic.main.scripts_controllers_to_add.view.*
import org.koin.core.KoinComponent
import smarthome.client.presentation.R
import smarthome.client.presentation.scripts.addition.controllers.epoxy.DevicesController
import smarthome.client.presentation.scripts.addition.graph.events.drag.DragOperationInfo
import smarthome.client.presentation.util.inflate
import smarthome.client.presentation.util.lifecycleOwner

class ControllersView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), KoinComponent {
    
    private val viewModel = ControllersViewViewModel() // todo scoped inject
    private val itemsController = DevicesController()
    private var onOpenMenuCallback: () -> Unit = {}
    
    
    init {
        inflate(R.layout.scripts_controllers_to_add)
    
        devices.layoutManager = LinearLayoutManager(context)
        devices.adapter = itemsController.adapter
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        
        lifecycleOwner?.let { lifecycleOwner ->
            lifecycleOwner.lifecycle.addObserver(viewModel)
            
            setupSlideableMenu(lifecycleOwner)
    
            viewModel.devices.observe(lifecycleOwner) { itemsController.setData(it, viewModel) }
            handleDroppedItemsAsCancelledDragAction()
        }
    }
    
    private fun setupSlideableMenu(lifecycle: LifecycleOwner) {
        setupSlideableMenu(this) {
            jumpTo.distinct().observe(lifecycle) { animate().translationX(it).duration = 0 }
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
        setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    
                    val dragInfo =
                        event.localState as? DragOperationInfo
                            ?: return@setOnDragListener false
                    viewModel.onDropped(dragInfo)
                }
            }
            true
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

