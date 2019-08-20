package smarthome.raspberry.presentation


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Job
import org.koin.android.ext.android.inject
import smarthome.raspberry.BuildConfig
import smarthome.raspberry.R
import smarthome.raspberry.domain.usecases.AuthUseCase
import smarthome.raspberry.domain.usecases.HomeUseCase
import smarthome.raspberry.input.InputController

private val TAG = MainActivity::class.java.simpleName
private val DEBUG = BuildConfig.DEBUG

class MainActivity : Activity() {
    private val authUseCase: AuthUseCase by inject()
    private val homeUseCase: HomeUseCase by inject()
    private val inputController: InputController by inject()
    private val job = Job()
    private var authenticationSubscription: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (DEBUG) Log.d(TAG, "onCreate")

        authenticationSubscription = authUseCase.isAuthenticated().subscribe {
            if (it == AuthUseCase.AuthStatus.NOT_SIGNED_IN) {
                startActivity(Intent(this, GoogleSignInActivity::class.java))
            }
        }
        inputController.init()

        homeUseCase.getHomeInfo()

    }

    override fun onDestroy() {
        super.onDestroy()
        authenticationSubscription?.dispose()
        job.cancel()
    }
}
