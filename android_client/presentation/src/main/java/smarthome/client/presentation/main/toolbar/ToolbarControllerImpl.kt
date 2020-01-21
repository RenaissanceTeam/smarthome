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
}

