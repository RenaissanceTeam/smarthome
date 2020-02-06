package smarthome.client.presentation.main.toolbar

class ToolbarControllerImpl(
    private val toolbarHolder: ToolbarHolder
) : ToolbarController {
    
    override fun setMenu(menuResource: Int, onMenuItemClick: (Int) -> Unit) {
        val toolbar = toolbarHolder.toolbar ?: return
        toolbar.inflateMenu(menuResource)
        toolbar.setOnMenuItemClickListener {
            onMenuItemClick(it.itemId)
            true
        }
    }
    
    override fun clearMenu() {
        val toolbar = toolbarHolder.toolbar ?: return
        toolbar.menu.clear()
    }
    
    override fun setTitle(value: String) {
        val toolbar = toolbarHolder.toolbar ?: return
        toolbar.title = value
    }
    
    override fun setNavigationIcon(resource: Int, onClick: () -> Unit) {
        val toolbar = toolbarHolder.toolbar ?: return
    
        toolbar.setNavigationIcon(resource)
        toolbar.setNavigationOnClickListener { onClick() }
    }
}

