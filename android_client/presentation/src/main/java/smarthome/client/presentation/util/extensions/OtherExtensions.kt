package smarthome.client.presentation.util.extensions

import kotlin.math.abs

fun Pair<Int, Int>.abs() = Pair(abs(first), abs(second))