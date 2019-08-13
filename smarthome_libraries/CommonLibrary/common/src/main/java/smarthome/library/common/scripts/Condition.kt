package smarthome.library.common.scripts

interface Condition {
    suspend fun satisfy(): Boolean
}
