package smarthome.client.util

fun String.truncate(max: Int): String {

    if (length <= max) return this
    
    return take(max) + "..."
}