package smarthome.client.presentation.scripts.addition.graph

data class DragOperationInfo(
    val currentSite: String,
    val dragTouch: Position,
    val blockType: String,
    val blockId: GraphBlockIdentifier,
    val onDropTo: (String) -> Boolean)

