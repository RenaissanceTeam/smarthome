package smarthome.raspberry.authentication.data.mapper

import com.google.firebase.auth.AuthCredential
import smarthome.raspberry.authentication.api.domain.Credentials

interface CredentialsToAuthCredentialsMapper {
    fun map(credentials: Credentials): AuthCredential
}

