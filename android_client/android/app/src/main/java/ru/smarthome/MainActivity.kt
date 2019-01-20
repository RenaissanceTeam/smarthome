package ru.smarthome

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.IdpResponse
import ru.smarthome.constants.Constants
import ru.smarthome.dashboard.DashboardFragment

class MainActivity : FragmentActivity() {

    private val viewModel
            by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
                .beginTransaction()
                .add(R.id.root_view, DashboardFragment(), DashboardFragment::class.java.simpleName)
                .commit()

        viewModel.needAuth.observe(this, Observer { needAuth -> if (needAuth) launchAuthActivity()})
    }

    override fun onStart() {
        super.onStart()
        viewModel.authCheck()
    }

    private fun launchAuthActivity() {
        startActivityForResult(viewModel.authUiWrapper.getAuthIntent(), Constants.RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == RESULT_OK) {
                viewModel.onAuthSuccessful()
            } else {
                viewModel.onAuthFailed()
            }
        }
    }
}