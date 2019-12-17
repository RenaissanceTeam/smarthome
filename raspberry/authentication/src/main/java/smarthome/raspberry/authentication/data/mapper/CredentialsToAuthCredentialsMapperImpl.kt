package smarthome.raspberry.authentication.data.mapper

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import smarthome.raspberry.authentication.api.domain.Credentials

class CredentialsToAuthCredentialsMapperImpl : CredentialsToAuthCredentialsMapper {
    override fun map(credentials: Credentials): AuthCredential {
        return GoogleAuthProvider.getCredential(credentials.idToken, null)
    }
}