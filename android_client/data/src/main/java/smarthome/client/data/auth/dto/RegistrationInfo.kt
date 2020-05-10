package smarthome.client.data.auth.dto

data class RegistrationInfo(
        val credentials: Credentials,
        val registrationCode: Long
)