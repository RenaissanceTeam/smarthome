package smarthome.client.presentation.util

data class Position(val x: Float, val y: Float) {
    
    operator fun minus(position: Position): Position {
        return copy(x = x - position.x, y = y - position.y)
    }
    
    operator fun plus(position: Position): Position {
        return copy(x = x + position.x, y = y + position.y)
    }
    
    
}

val emptyPosition = Position(0f, 0f)

fun IntArray.toPosition(): Position {
    if (size != 2) return emptyPosition
    
    return Position(this[0].toFloat(), this[1].toFloat())
}