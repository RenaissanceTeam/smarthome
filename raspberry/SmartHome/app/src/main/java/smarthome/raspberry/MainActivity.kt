package smarthome.raspberry

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


import smarthome.raspberry.auth.GoogleSignInActivity
import smarthome.raspberry.model.SmartHomeRepository
import smarthome.raspberry.server.StoppableServer
import smarthome.raspberry.server.UdpServer
import smarthome.raspberry.server.WebServer
import smarthome.raspberry.utils.HomeController

private val TAG = MainActivity::class.java.simpleName
private val DEBUG = BuildConfig.DEBUG
private val AUTH_ACTIVITY_REQUEST_CODE = 12312

class MainActivity : Activity() {
    private var httpServer: WebServer? = null
    private var udpServer: UdpServer? = null
    private val mAuth: FirebaseAuth? = null

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
                    SmartHomeRepository.listenForCloudChanges()
                } catch (e: Throwable) {
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
