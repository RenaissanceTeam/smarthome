package smarthome.client.presentation.util

data class Position(val x: Float, val y: Float) {
    
    operator fun minus(position: Position): Position {
        return copy(x = x - position.x, y = y - position.y)
    }
}

val emptyPosition = Position(-1f, -1f)