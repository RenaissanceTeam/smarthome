package smarthome.client.presentation.scripts.addition.graph

data class GraphBlock(val id: GraphBlockIdentifier,
                      val position: Position = emptyPosition,
                      val type: String)