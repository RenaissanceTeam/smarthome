package smarthome.raspberry

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*


import smarthome.raspberry.auth.GoogleSignInActivity
import smarthome.raspberry.model.SmartHomeRepository
import smarthome.raspberry.server.UdpServer
import smarthome.raspberry.server.WebServer
import smarthome.raspberry.service.DeviceObserver

private val TAG = MainActivity::class.java.simpleName
private val DEBUG = BuildConfig.DEBUG
private val AUTH_ACTIVITY_REQUEST_CODE = 12312

class MainActivity : Activity() {
    private var httpServer: WebServer? = null
    private var udpServer: UdpServer? = null

    val deviceObserver: DeviceObserver = DeviceObserver.getInstance()

    private val isAuthenticated = FirebaseAuth.getInstance().currentUser != null
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

        if (!isAuthenticated) {
            startActivity(Intent(this, GoogleSignInActivity::class.java))
        } else {
            uiScope.launch {
                try {
                    SmartHomeRepository.init(applicationContext)
                    delay(5000)
                    SmartHomeRepository.listenForCloudChanges() // TODO: normal fix
                    SmartHomeRepository.subscribeToMessageQueue()
                    deviceObserver.start()
                } catch (e: Throwable) {
                    Log.d(TAG, "Initialization or cloud change listener set up failed", e)
                    Toast.makeText(baseContext, e.message, Toast.LENGTH_SHORT).show()
                }
            }
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
}
