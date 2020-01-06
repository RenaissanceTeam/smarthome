package smarthome.raspberry.authentication.data

import smarthome.raspberry.authentication.api.domain.entity.Credentials

interface AuthenticatorAdapter {
    fun authenticate(credentials: Credentials)
}

