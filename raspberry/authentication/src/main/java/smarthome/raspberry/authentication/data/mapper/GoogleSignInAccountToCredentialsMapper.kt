package smarthome.raspberry.authentication.data.mapper


import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import smarthome.raspberry.authentication.api.domain.Credentials

interface GoogleSignInAccountToCredentialsMapper {
    fun map(account: GoogleSignInAccount): Credentials
}