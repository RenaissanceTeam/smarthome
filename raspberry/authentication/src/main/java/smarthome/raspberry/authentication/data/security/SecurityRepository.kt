package smarthome.raspberry.authentication.data.security

interface SecurityRepository {
    fun getAuthenticatedUsername(): String
}