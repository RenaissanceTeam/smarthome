package smarthome.client.presentation.scripts.addition.graph

import android.view.ViewGroup

interface GraphBlockFactory {
    fun inflate(into: ViewGroup, block: GraphBlock): GraphDraggable
}