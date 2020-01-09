package smarthome.client.data.api.auth

interface LoginCommand {
    suspend fun run(login: String, password: String): String
}