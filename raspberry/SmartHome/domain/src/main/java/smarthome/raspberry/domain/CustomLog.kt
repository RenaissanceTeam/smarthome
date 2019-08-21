package smarthome.raspberry.domain

object Log {
    var logger: ((String) -> Unit)? = null
}

fun Log.log(s: String) {
    logger?.invoke(s)
}