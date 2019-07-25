package smarthome.raspberry.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log

import kotlinx.coroutines.*
import smarthome.raspberry.BuildConfig
import smarthome.raspberry.R


import smarthome.raspberry.domain.usecases.AuthUseCase
import smarthome.raspberry.domain.usecases.DevicesUseCase
import smarthome.raspberry.domain.usecases.HomeUseCase

private val TAG = MainActivity::class.java.simpleName
private val DEBUG = BuildConfig.DEBUG
private val AUTH_ACTIVITY_REQUEST_CODE = 12312

class MainActivity : Activity() {
    private val authUseCase: AuthUseCase = TODO()
    private val devicesUseCase: DevicesUseCase = TODO()
    private val homeUseCase: HomeUseCase = TODO()
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (DEBUG) Log.d(TAG, "onCreate")

    }

    override fun onResume() {
        super.onResume()

        if (!authUseCase.isAuthenticated()) {
            startActivity(Intent(this, GoogleSignInActivity::class.java))
        } else {
            uiScope.launch { homeUseCase.start() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
