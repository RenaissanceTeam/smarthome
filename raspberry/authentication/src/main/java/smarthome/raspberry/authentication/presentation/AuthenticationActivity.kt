package smarthome.raspberry.authentication.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_google_sign_in.*
import org.koin.android.ext.android.inject
import org.koin.android.scope.currentScope
import org.koin.core.parameter.parametersOf
import smarthome.raspberry.authentication.R
import smarthome.raspberry.authentication.data.mapper.GoogleSignInAccountToCredentialsMapper

class AuthenticationActivity : AppCompatActivity(), AuthenticationView {
    
    private val apiClient: GoogleApiClient by inject()
    private val googleSignInAccountToCredentialsMapper: GoogleSignInAccountToCredentialsMapper by inject()
    private val presenter: AuthenticationPresenter by currentScope.inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_sign_in)

        sign_in_button.setOnClickListener { presenter.signIn() }
        sign_out_button.setOnClickListener { presenter.sighOut() }
        delete_all.setOnClickListener { presenter.deleteAll() }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                        ?: throw ApiException(Status.RESULT_DEAD_CLIENT)
                val credentials = googleSignInAccountToCredentialsMapper.map(account)
                
                presenter.onAuthenticationSuccess(credentials)
            } catch (e: ApiException) {
                presenter.onAuthenticationFail()
            }
        }
    }

    override fun showDeleteAllConfirmationDialog(action: () -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.delete_all)
                .setMessage(R.string.disconnect_confirmation)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok) { _, _ -> action() }
                .setNegativeButton(android.R.string.cancel, null)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun startAuthentication() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun setState(state: AuthenticationState) {
        when (state) {
            is AuthenticationState.Loading -> {
                status.text = "Signing in..."
            }
            is AuthenticationState.Empty -> {
                title_text.setText(R.string.google_sign_in_title_text)
                status.text = null

                sign_in_button.visibility = View.VISIBLE
                sign_out_and_disconnect.visibility = View.GONE
            }
            is AuthenticationState.Error -> {
                status.text = "Can't login"
                Toast.makeText(this, state.reason, Toast.LENGTH_SHORT).show()
            }
            is AuthenticationState.Authenticated -> {
                title_text.setText(R.string.google_signed_in_title_text)
                status.text = getString(R.string.google_status_fmt, state.user.email)

                sign_in_button.visibility = View.GONE
                sign_out_and_disconnect.visibility = View.VISIBLE
            }
        }
    }
    
    companion object {
        private const val RC_SIGN_IN = 9001
    }
}
