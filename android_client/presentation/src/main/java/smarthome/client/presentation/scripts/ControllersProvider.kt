package smarthome.client.presentation.scripts



interface ControllersProvider {
    suspend fun getControllers(): List<Controller>
}