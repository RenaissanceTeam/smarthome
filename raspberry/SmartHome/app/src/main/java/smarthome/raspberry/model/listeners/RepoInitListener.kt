package smarthome.raspberry.model.listeners

interface RepoInitListener {
    suspend fun onInitializationComplete()
}
