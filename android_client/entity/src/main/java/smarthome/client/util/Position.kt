package smarthome.client.util

data class Position(val x: Int, val y: Int) {
    operator fun minus(position: Position): Position {
        return copy(x = x - position.x, y = y - position.y)
    }
    
    operator fun plus(position: Position): Position {
        return copy(x = x + position.x, y = y + position.y)
    }
}

val emptyPosition = Position(0, 0)

fun IntArray.toPosition(): Position {
    if (size != 2) return emptyPosition
    
    return Position(this[0], this[1])
}