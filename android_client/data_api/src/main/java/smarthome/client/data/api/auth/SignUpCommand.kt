package smarthome.client.data.api.auth

interface SignUpCommand {
    suspend fun run(login: String, password: String, registrationCode: Long): String
}