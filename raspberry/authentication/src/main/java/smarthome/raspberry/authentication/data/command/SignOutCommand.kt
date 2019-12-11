package smarthome.raspberry.authentication.data.command

interface SignOutCommand {
    suspend fun execute()
}