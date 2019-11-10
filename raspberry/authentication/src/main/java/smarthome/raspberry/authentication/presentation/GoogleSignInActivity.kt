package smarthome.raspberry.authentication.presentation

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_google_sign_in.*
import org.koin.android.ext.android.inject
import smarthome.raspberry.authentication.R
import smarthome.raspberry.home_api.presentation.MainFlowLauncher

class GoogleSignInActivity : FragmentActivity(),
        GoogleApiClient.OnConnectionFailedListener {
    companion object {

        private val TAG = "Sign in activity"
        private val RC_SIGN_IN = 9001
    }

    private val mAuth: FirebaseAuth by inject()
    private val apiClient: GoogleApiClient by inject()
    private val mainFlowLauncher: MainFlowLauncher by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_sign_in)


        sign_in_button.setOnClickListener { signIn() }
        sign_out_button.setOnClickListener { signOut() }
        disconnect_button.setOnClickListener { disconnect() }

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

// todo provide
//        apiClient = GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build()
//
//        mAuth = FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this@GoogleSignInActivity, "Auth failed, try later",
                               Toast.LENGTH_SHORT).show()
                updateUI(null)
            }
        }
    }


    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        showProgressDialog()

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        mainFlowLauncher.launch()
                    } else {
                        Toast.makeText(applicationContext, "Authorization failed.",
                                       Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                    hideProgressDialog()
                }
    }

    private fun disconnect() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.disconnect)
                .setMessage(R.string.disconnect_confirmation)
                .setCancelable(true)
                .setPositiveButton("yes") { dialogInterface, i -> revokeAccess() }
                .setNegativeButton("cancel", null)
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    private fun signOut() {
        // Firebase sign out
        mAuth.signOut()

        // Google sign out
        Auth.GoogleSignInApi.signOut(apiClient).setResultCallback { updateUI(null) }
    }

    private fun revokeAccess() {
        // delete user data on device
        val editor = PreferenceManager.getDefaultSharedPreferences(applicationContext).edit()
        editor.clear()
        editor.apply()

        application.onCreate()

        // Firebase sign out
        mAuth.signOut()

        // Google revoke access
        Auth.GoogleSignInApi.signOut(apiClient).setResultCallback { updateUI(null) }
    }

    private fun updateUI(user: FirebaseUser?) {
        hideProgressDialog()
        if (user != null) {
            title_text.setText(R.string.google_signed_in_title_text)
            status.text = getString(R.string.google_status_fmt, user.email)

            sign_in_button.visibility = View.GONE
            sign_out_and_disconnect.visibility = View.VISIBLE
        } else {
            title_text.setText(R.string.google_sign_in_title_text)
            status.text = null

            sign_in_button.visibility = View.VISIBLE
            sign_out_and_disconnect.visibility = View.GONE
        }
    }


    fun showProgressDialog() {
        TODO()
    }

    fun hideProgressDialog() {
        TODO()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Toast.makeText(this@GoogleSignInActivity, "Connection failed", Toast.LENGTH_SHORT).show()
    }
}
