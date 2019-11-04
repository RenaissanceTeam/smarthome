package smarthome.raspberry.presentation


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import smarthome.raspberry.BuildConfig
import smarthome.raspberry.R
import smarthome.raspberry.authentication.AuthUseCase
import smarthome.raspberry.home.HomeUseCase
import smarthome.raspberry.input.InputController

private val TAG = MainActivity::class.java.simpleName
private val DEBUG = BuildConfig.DEBUG

class MainActivity : Activity() {
    private val authUseCase: smarthome.raspberry.authentication.AuthUseCase by inject()
    private val homeUseCase: smarthome.raspberry.home.HomeUseCase by inject()
    private val inputController: InputController by inject()
    private var authenticationSubscription: Disposable? = null
    private var homeInfoSubscription: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (DEBUG) Log.d(TAG, "onCreate")

        authenticationSubscription = authUseCase.isAuthenticated()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it == smarthome.raspberry.authentication.AuthUseCase.AuthStatus.NOT_SIGNED_IN) {
                        startActivity(Intent(this, GoogleSignInActivity::class.java))
                    }

                    auth_status.text = it.toString()
                }
        inputController.init()

        homeInfoSubscription = homeUseCase.getHomeInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    home.text = it.homeId
                    user.text = it.userId
                }

        homeUseCase.launchStateMachine()

        smarthome.raspberry.domain.Log.logger = { Log.e("DomainLog", it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        authenticationSubscription?.dispose()
        homeInfoSubscription?.dispose()
    }
}
