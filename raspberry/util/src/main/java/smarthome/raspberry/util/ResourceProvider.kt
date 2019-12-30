package smarthome.raspberry.util


class ResourceProvider {
    val resources: Resources
        get() = TODO()
}

interface Resources {
    fun getString(key: String): String
}