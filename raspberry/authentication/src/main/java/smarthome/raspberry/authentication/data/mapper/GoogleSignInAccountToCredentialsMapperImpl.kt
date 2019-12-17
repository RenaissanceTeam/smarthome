package smarthome.raspberry.authentication.data.mapper

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import smarthome.raspberry.authentication.api.domain.Credentials

class GoogleSignInAccountToCredentialsMapperImpl : GoogleSignInAccountToCredentialsMapper {
    override fun map(account: GoogleSignInAccount): Credentials {
        return Credentials(account.idToken.orEmpty())
    }
}