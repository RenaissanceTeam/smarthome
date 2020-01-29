package smarthome.client.presentation.scripts.addition.graph.events.drag

import smarthome.client.presentation.scripts.addition.graph.Position

class ControllerDragOperationInfo(val id: Long,
                                  status: String,
                                  from: String = UNKNOWN,
                                  to: String = UNKNOWN,
                                  dragTouch: Position
) : DragOperationInfo(status, from, to, dragTouch)