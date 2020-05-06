package smarthome.client.presentation.scripts.setup.graph.blockviews.dependency

import smarthome.client.util.Position
import kotlin.math.abs
import kotlin.math.sign

class BorderIntersectionHelper {
    fun tipPositionsWithIntersection(start: DependencyTip, end: DependencyTip): Pair<Position, Position> {

        val delta = (start.center - end.center)
        if (abs(delta.x) <= start.width / 2 && abs(delta.y) <= start.height / 2) {
            return Pair(start.center, start.center)
        }

        return Pair(
                calculateStart(start, delta),
                calculateEnd(end, delta)
        )
    }

    private fun calculateStart(start: DependencyTip, delta: Position): Position {
        return calculateBorderIntersection(start, delta, -1)
    }

    private fun proportion(bigBottom: Int, smallSide: Int, bigSide: Int): Int {
        if (bigSide == 0) return if (bigBottom > 0) Int.MAX_VALUE else Int.MIN_VALUE

        return bigBottom * smallSide / bigSide * bigSide.sign
    }

    private fun calculateBorderIntersection(start: DependencyTip, delta: Position, factor: Int): Position {
        return Position(
                start.center.x + factor * proportion(delta.x, start.height / 2, delta.y)
                        .coerceIn(-start.width / 2, start.width / 2),
                start.center.y + factor * proportion(delta.y, start.width / 2, delta.x)
                        .coerceIn(-start.height / 2, start.height / 2)
        )
    }

    private fun calculateEnd(start: DependencyTip, delta: Position): Position {
        return calculateBorderIntersection(start, delta, 1)
    }
}