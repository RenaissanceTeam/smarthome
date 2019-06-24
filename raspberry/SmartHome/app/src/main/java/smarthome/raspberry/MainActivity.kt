package smarthome.raspberry

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log

import kotlinx.coroutines.*


import smarthome.raspberry.auth.GoogleSignInActivity
import smarthome.raspberry.domain.usecases.AuthUseCase
import smarthome.raspberry.domain.usecases.DevicesUseCase
import smarthome.raspberry.domain.usecases.HomeUseCase
import smarthome.raspberry.model.SmartHomeRepository
import smarthome.raspberry.model.listeners.RepoInitListener
import smarthome.raspberry.arduinodevices.server.UdpServer
import smarthome.raspberry.arduinodevices.server.WebServer
import smarthome.raspberry.service.DeviceObserver

private val TAG = MainActivity::class.java.simpleName
private val DEBUG = BuildConfig.DEBUG
private val AUTH_ACTIVITY_REQUEST_CODE = 12312

class MainActivity : Activity(), RepoInitListener {
    private var httpServer: WebServer? = null
    private var udpServer: UdpServer? = null
    private val authUseCase: AuthUseCase = TODO()
    private val devicesUseCase: DevicesUseCase = TODO()
    private val homeUseCase: HomeUseCase = TODO()


    val deviceObserver: DeviceObserver = DeviceObserver.getInstance()


    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (DEBUG) Log.d(TAG, "onCreate")

        httpServer = WebServer()
        udpServer = UdpServer()
    }

    override fun onStart() {
        super.onStart()
        httpServer?.startServer()
        udpServer?.startServer()
    }

    override fun onResume() {
        super.onResume()

        if (!authUseCase.isAuthenticated()) {
            startActivity(Intent(this, GoogleSignInActivity::class.java))
        } else {
            uiScope.launch { homeUseCase.start() }
        }
    }

    override fun onStop() {
        super.onStop()
        httpServer?.stopServer()
        udpServer?.stopServer()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override suspend fun onInitializationComplete() {
        SmartHomeRepository.listenForCloudChanges()
        SmartHomeRepository.subscribeToMessageQueue()
        deviceObserver.start()
    }
}
