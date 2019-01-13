package ru.smarthome

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.smarthome.constants.Constants
import ru.smarthome.library.BaseController
import ru.smarthome.library.IotDevice
import ru.smarthome.library.RaspberryResponse
import ru.smarthome.library.SmartHome

// todo should not be a singleton
object Model {
    private val TAG = Model::class.java.simpleName

    private var smartHomeState: SmartHome? = null
    private var controllers: MutableList<BaseController> = mutableListOf()

    private val raspberryApi: RaspberryApi
        get() {
            val gson = GsonBuilder().setLenient().create()

            val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.RASPBERRY_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

            return retrofit.create(RaspberryApi::class.java)
        }

    fun requestHomeStateFromRaspberry(): MutableLiveData<MutableList<BaseController>> {
        val newHome = MutableLiveData<MutableList<BaseController>>()

        raspberryApi.getSmartHomeState().enqueue {
            onResponse = { response ->
                if (response.isSuccessful) {
                    if (BuildConfig.DEBUG) Log.d(TAG, "responseBody= ${response.body()}")
                    response.body()?.let { newHomeState ->
                        smartHomeState = newHomeState
                        controllers = getAllControllersFrom(newHomeState)
                        newHome.value = controllers
                    }
                    // todo notify about error
                } else {
                    // todo notify about error
                }
            }

            onFailure = {
                handleRequestFailure(it)
            }
        }
        return newHome
    }

    fun getDevice(controller: BaseController) : IotDevice? {
        return smartHomeState?.devices?.find { it.controllers.contains(controller) }
    }

    private fun getAllControllersFrom(sh: SmartHome): MutableList<BaseController> {
        val controllers = mutableListOf<BaseController>()
        sh.devices.forEach { controllers.addAll(it.controllers) }
        return controllers
    }

    // todo should return LiveData<ControllerState> but now String is controller state
    fun readController(controller: BaseController): MutableLiveData<MutableList<BaseController>> {

        val newState = MutableLiveData<MutableList<BaseController>>()
        val controllerGuid = controller.guid

        raspberryApi.readControllerState(controllerGuid).enqueue {
            onResponse = { response ->
                if (response.isSuccessful) {
                    handleReadControllerStateResponse(controller, response, newState)
                } else {
                    handleResponseError(response)
                }
            }

            onFailure = { handleRequestFailure(it) }
        }
        return newState
    }

    fun changeControllerState(controller: BaseController,
                              value: String): MutableLiveData<MutableList<BaseController>> {
        val newState = MutableLiveData<MutableList<BaseController>>()
        val controllerGuid = controller.guid

        raspberryApi.changeControllerState(controllerGuid, value).enqueue {
            onResponse = { response ->
                if (response.isSuccessful) {
                    handleReadControllerStateResponse(controller, response, newState)
                } else {
                    handleResponseError(response)
                }
            }

            onFailure = { handleRequestFailure(it) }
        }
        return newState
    }

    private fun handleReadControllerStateResponse(controller: BaseController,
                                                  response: Response<RaspberryResponse>,
                                                  newState: MutableLiveData<MutableList<BaseController>>) {
        controllers.find { it == controller }?.state = response.body()?.response
        newState.value = controllers
    }

    private fun handleResponseError(response: Response<RaspberryResponse>) {
        val message = "Returned code: ${response.code()}, body=${response.raw().body()?.string()}"
        Log.d(TAG, "returned not successful response $message")
        // todo notify user about error
    }

    private fun handleRequestFailure(it: Throwable?) {
        Log.d(TAG, "onFailure: $it")
        // todo notify user about error
    }

//    private fun auth() {
//        val providers = listOf(AuthUI.IdpConfig.GoogleBuilder().build())
//
//        startActivityForResult(AuthUI.getInstance()
//                .createSignInIntentBuilder()
//                .setAvailableProviders(providers)
//                .build(), Constants.RC_SIGN_IN)
//    }
//
//    // todo move to model layer
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == Constants.RC_SIGN_IN) {
//            val response = IdpResponse.fromResultIntent(data)
//
//            if (resultCode == AppCompatActivity.RESULT_OK) {
//                val user = FirebaseAuth.getInstance().currentUser
//            } else {
//                // TODO: guide user to the xuy
//            }
//        }
//    }
}