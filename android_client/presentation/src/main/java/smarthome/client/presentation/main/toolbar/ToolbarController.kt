package smarthome.client.presentation.main.toolbar

interface ToolbarController {
    fun setMenu(menuResource: Int, onMenuItemClick: (Int) -> Unit)
    fun clearMenu()
    fun setNavigationIcon(resource: Int, onClick: () -> Unit)
    fun setTitle(value: String)
}
