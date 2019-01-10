package ru.smarthome

import android.content.Intent
import android.os.Bundle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.smarthome.constants.Constants.RASPBERRY_URL
import ru.smarthome.constants.Constants.RC_SIGN_IN

class MainActivity : AppCompatActivity() {
    private var refreshLayout: SwipeRefreshLayout? = null
    private var controllers: RecyclerView? = null
    private var adapter: ControllersAdapter? = null
    val TAG = "MainActivity"

    companion object {
        val raspberryApi: RaspberryApi
            get() {
                val gson = GsonBuilder().setLenient().create()

                val retrofit = Retrofit.Builder()
                        .baseUrl(RASPBERRY_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()

                return retrofit.create(RaspberryApi::class.java)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        refreshLayout = findViewById(R.id.refresh_controllers)
        controllers = findViewById(R.id.controllers)
        controllers?.layoutManager = LinearLayoutManager(this)

        adapter = ControllersAdapter(layoutInflater)
        controllers?.adapter = adapter

        refreshLayout?.setOnRefreshListener { requestInfoFromRaspberry() }
//        auth()

        requestInfoFromRaspberry() // todo after auth successful
    }

    private fun stopRefreshing() {
        refreshLayout?.isRefreshing = false
    }

    private fun auth() {
        val providers = listOf<AuthUI.IdpConfig>(AuthUI.IdpConfig.GoogleBuilder().build())

        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(), RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
            } else {
                // TODO: guide user to the xuy
            }
        }
    }

    private fun requestInfoFromRaspberry() {
        raspberryApi.getSmartHomeState().enqueue {
            onResponse = {
                Log.d(TAG, "onResponse: " + it.body())
                adapter?.loadNewHome(it.body())
                stopRefreshing()
            }

            onFailure = {
                Log.d(TAG, "onFailure: $it")
                stopRefreshing()
            }
        }

    }
}