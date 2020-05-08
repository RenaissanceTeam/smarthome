package smarthome.raspberry.authentication.data.dto

import smarthome.raspberry.authentication.api.domain.entity.Credentials

data class SignUpInfo(
        val credentials: Credentials,
        val registrationCode: Long
)