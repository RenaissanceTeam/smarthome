package smarthome.raspberry.authentication.api.domain.entity

data class RegistrationInfo(val credentials: Credentials, val roles: Set<String>)