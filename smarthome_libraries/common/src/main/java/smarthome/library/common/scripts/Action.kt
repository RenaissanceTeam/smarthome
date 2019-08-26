package smarthome.library.common.scripts

interface Action {
    suspend fun run()
}